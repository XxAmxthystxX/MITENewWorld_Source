package com.mitenewworld.devtools;
import com.mitenewworld.MITENewWorld;

import com.google.gson.*;
import net.minecraft.nbt.*;
import java.io.*;
import java.nio.file.*;
import java.util.logging.*;

public class NbtJsonConverter {

    // 初始化日志记录器
    private static final Logger LOGGER = Logger.getLogger(NbtJsonConverter.class.getName());

    // 方法：将 NBT 文件转换为 JSON 文件
    public static void nbtToJson(String nbtFilePath, String jsonFilePath) {
        LOGGER.info("开始将 NBT 文件转换为 JSON 文件...");
        try {
            // 加载 NBT 文件
            NbtCompound nbt = NbtIo.readCompressed(Path.of(nbtFilePath), new NbtSizeTracker(102400000, 256));

            // 创建一个带格式化选项的 Gson 实例
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // 将 NBT 转换为 JSON
            JsonElement jsonElement = nbtToJsonElement(nbt);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // 保存为 JSON 文件
            try (FileWriter writer = new FileWriter(jsonFilePath)) {
                gson.toJson(jsonObject, writer);
            }
            LOGGER.info("NBT 文件已成功转换为 JSON 文件：" + jsonFilePath);
        } catch (IOException e) {
            LOGGER.warning("转换 NBT 文件为 JSON 时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 方法：将 NBT 复合标签（Compound）转换为 JSON 元素
    public static JsonElement nbtToJsonElement(NbtCompound nbt) {
        JsonObject json = new JsonObject();
        for (String key : nbt.getKeys()) {
            var tag = nbt.get(key);
            json.add(key, tagToJsonElement(tag));
        }
        return json;
    }

    // 方法：将 NBT 列表标签转换为 JSON 数组
    public static JsonElement nbtListToJson(NbtList list) {
        JsonArray jsonArray = new JsonArray();
        for (var tag : list) {
            jsonArray.add(tagToJsonElement(tag));
        }
        return jsonArray;
    }

    // 核心转换：将任意 NBT 标签转换为 JsonElement
    private static JsonElement tagToJsonElement( NbtElement tag) {
        if (tag instanceof NbtCompound) {
            return nbtToJsonElement((NbtCompound) tag);
        } else if (tag instanceof NbtList) {
            return nbtListToJson((NbtList) tag);
        } else if (tag instanceof NbtByteArray) {
            byte[] bytes = ((NbtByteArray) tag).getByteArray();
            JsonArray array = new JsonArray();
            for (byte b : bytes) {
                array.add(b);
            }
            return array;
        } else if (tag instanceof NbtIntArray) {
            int[] ints = ((NbtIntArray) tag).getIntArray();
            JsonArray array = new JsonArray();
            for (int i : ints) {
                array.add(i);
            }
            return array;
        } else if (tag instanceof NbtLongArray) {
            long[] longs = ((NbtLongArray) tag).getLongArray();
            JsonArray array = new JsonArray();
            for (long l : longs) {
                array.add(l);
            }
            return array;
        } else {
            // 基本类型：直接提取原始值
            return primitiveToJsonPrimitive(tag);
        }
    }

    // 将基本 NBT 类型转换为 JsonPrimitive
    private static JsonPrimitive primitiveToJsonPrimitive( NbtElement tag) {
        if (tag instanceof NbtByte) {
            return new JsonPrimitive(((NbtByte) tag).byteValue());
        } else if (tag instanceof NbtShort) {
            return new JsonPrimitive(((NbtShort) tag).shortValue());
        } else if (tag instanceof NbtInt) {
            return new JsonPrimitive(((NbtInt) tag).intValue());
        } else if (tag instanceof NbtLong) {
            return new JsonPrimitive(((NbtLong) tag).longValue());
        } else if (tag instanceof NbtFloat) {
            return new JsonPrimitive(((NbtFloat) tag).floatValue());
        } else if (tag instanceof NbtDouble) {
            return new JsonPrimitive(((NbtDouble) tag).doubleValue());
        } else if (tag instanceof NbtString) {
            return new JsonPrimitive(((NbtString) tag).asString().get());
        } else {
            throw new IllegalArgumentException("Unhandled NBT type: " + tag.getClass());
        }
    }

    // 方法：将 JSON 文件转换为 NBT 文件
    public static void jsonToNbt(String jsonFilePath, String nbtFilePath) {
        LOGGER.info("开始将 JSON 文件转换为 NBT 文件...");
        try {
            Gson gson = new Gson();
            try (Reader reader = Files.newBufferedReader(Paths.get(jsonFilePath))) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                // 将 JSON 对象转换为 NBT 复合标签
                NbtCompound nbt = jsonToNbtCompound(jsonObject);

                // 保存为 NBT 文件
                NbtIo.writeCompressed(nbt, Path.of(nbtFilePath));
                LOGGER.info("JSON 文件已成功转换为 NBT 文件：" + nbtFilePath);
            }
        } catch (IOException e) {
            LOGGER.severe("转换 JSON 文件为 NBT 时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 方法：将 JSON 对象转换为 NBT 复合标签
    public static NbtCompound jsonToNbtCompound(JsonObject jsonObject) {
        NbtCompound nbt = new NbtCompound();

        for (String key : jsonObject.keySet()) {
            JsonElement element = jsonObject.get(key);

            if (element.isJsonObject()) {
                // 递归处理 JSON 对象，转换为 NbtCompound
                nbt.put(key, jsonToNbtCompound(element.getAsJsonObject()));
            } else if (element.isJsonArray()) {
                // 递归处理 JSON 数组，转换为 NbtList
                nbt.put(key, jsonListToNbt(element.getAsJsonArray()));
            } else if (element.isJsonPrimitive()) {
                // 直接处理 JSON 原始类型
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                if (primitive.isString()) {
                    nbt.put(key, NbtString.of(primitive.getAsString()));
                } else if (primitive.isNumber()) {
                    // 尝试智能识别数字类型，这里简化为 int，可根据需要扩展
                    Number num = primitive.getAsNumber();
                    if (num instanceof Double && num.doubleValue() % 1 != 0) {
                        nbt.put(key, NbtDouble.of(num.doubleValue()));
                    } else {
                        nbt.put(key, NbtInt.of(num.intValue()));
                    }
                } else if (primitive.isBoolean()) {
                    nbt.put(key, NbtByte.of((byte) (primitive.getAsBoolean() ? 1 : 0)));
                }
            } else {
                // 对于非预期的类型，进行警告或处理
                LOGGER.warning("无法识别的 JSON 元素类型: " + element.getClass().getName());
            }
        }

        return nbt;
    }

    // 方法：将 JSON 数组转换为 NBT 列表
    public static NbtList jsonListToNbt(JsonArray jsonArray) {
        NbtList nbtList = new NbtList();

        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                // 递归处理 JSON 对象，转换为 NbtCompound
                nbtList.add(jsonToNbtCompound(element.getAsJsonObject()));
            } else if (element.isJsonArray()) {
                // 递归处理 JSON 数组，转换为 NbtList
                nbtList.add(jsonListToNbt(element.getAsJsonArray()));
            } else if (element.isJsonPrimitive()) {
                // 直接处理 JSON 原始类型
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                if (primitive.isString()) {
                    nbtList.add(NbtString.of(primitive.getAsString()));
                } else if (primitive.isNumber()) {
                    Number num = primitive.getAsNumber();
                    if (num instanceof Double && num.doubleValue() % 1 != 0) {
                        nbtList.add(NbtDouble.of(num.doubleValue()));
                    } else {
                        nbtList.add(NbtInt.of(num.intValue()));
                    }
                } else if (primitive.isBoolean()) {
                    nbtList.add(NbtByte.of((byte) (primitive.getAsBoolean() ? 1 : 0)));
                }
            }
        }

        return nbtList;
    }

    // 主方法：示例用法
    public static void main(String[] args) {
        String nbtFilePath = "G:\\mc\\sophisticatedbackpacks1.dat"; // 输入 NBT 文件路径
        String jsonFilePath = "G:\\mc\\sophisticatedbackpacks.json"; // 输出 JSON 文件路径
        //String outputNbtFilePath = "path_to_output_nbt_file.dat"; // 输出 NBT 文件路径

        // 将 NBT 转换为 JSON
        //nbtToJson(nbtFilePath, jsonFilePath);

        // 将 JSON 转换回 NBT
        jsonToNbt(jsonFilePath, nbtFilePath);
    }
}
