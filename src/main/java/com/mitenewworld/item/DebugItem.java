package com.mitenewworld.item;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModArmorItem;
import com.mitenewworld.item.metal.ModToolItem;
import com.mitenewworld.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DebugItem extends Item {
    public DebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        ItemStack stack = user.getStackInHand(hand);

        // ===== 1. 自定义金属配比 =====
        Map<String, Float> metals = new LinkedHashMap<>();
        metals.put("iridium",    1.00f);

        // ===== 2. 自定义强制共鸣（形态标记）=====
        // 可选: "nugget" / "block" / "chain" / 空集
        Set<String> forced = Set.of();

        // ===== 3. 生成 AlloyComponent =====
        // of() 内部会量化到 1/1000，低于 0.0005 归零
        AlloyComponent comp = AlloyComponent.compute(metals , forced);


        ItemStack itemStack2 = ((ModArmorItem)(ModItems.ALLOY_CHESTPLATE)).createFromAlloy(comp);

        user.setStackInHand(hand, itemStack2);

        // ===== 5. 调试输出 =====
        if (user instanceof ServerPlayerEntity sp) {
            sp.sendMessage(Text.literal("§a=== 合金测试 ==="), false);
            sp.sendMessage(Text.literal("§7显示名: §f" + comp.display()), false);
            sp.sendMessage(Text.literal("§7主导: §f" + comp.dominant()), false);
            sp.sendMessage(Text.literal("§7耐久: §f" + comp.durability()), false);
            sp.sendMessage(Text.literal("§7硬度: §f" + comp.hardness()), false);
            sp.sendMessage(Text.literal("§7韧性: §f" + comp.toughness()), false);
            sp.sendMessage(Text.literal("§7附魔: §f" + comp.enchantRarity()), false);
            sp.sendMessage(Text.literal("§7重量: §f" + comp.weight()), false);
            sp.sendMessage(Text.literal("§7共鸣: §f" + comp.activeSynergies()), false);
        }

        return ActionResult.SUCCESS;
    }


}
