package com.mitenewworld.mixin;


import com.google.common.collect.Iterators;
import com.mitenewworld.MITENewWorld;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Stream;
///弃用原因：原版注册已经由写死的注册改为工厂方法，这样mixin已经可以拦截到new block，不需要清理注册表
@Deprecated
@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> {
    @Mutable
    @Final
    @Shadow
    private final RegistryKey<? extends Registry<T>> key;
    @Mutable
    @Shadow
    private ObjectList<RegistryEntry.Reference<T>> rawIdToEntry = new ObjectArrayList<>(256);
    @Mutable
    @Shadow
    private Reference2IntMap<T> entryToRawId = Util.make(new Reference2IntOpenHashMap<>(), map -> map.defaultReturnValue(-1));
    @Shadow
    private final Map<Identifier, RegistryEntry.Reference<T>> idToEntry = new HashMap<>();
    @Shadow
    private final Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry = new HashMap<>();
    @Shadow
    private final Map<T, RegistryEntry.Reference<T>> valueToEntry = new IdentityHashMap<>();
    @Shadow
    private final Map<RegistryKey<T>, RegistryEntryInfo> keyToEntryInfo = new IdentityHashMap<>();
    @Shadow
    private boolean frozen;
    @Nullable
    @Shadow
    private Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;

    public SimpleRegistryMixin(RegistryKey<? extends Registry<T>> key) {
        this.key = key;
    }

   /* @Inject(method = "freeze", at = @At("HEAD"), cancellable = true)
    public void freeze(CallbackInfoReturnable<Registry<T>> cir) {
        cir.cancel();
        if (this.frozen) {
            cir.setReturnValue((Registry<T>) this);
            return;
        }

        this.frozen = true;
        // 确保所有条目正确绑定
        this.valueToEntry.forEach((value, entry) -> {
            if (value != null && entry != null) {
                entry.setValue(value);
            }
        });

        // 获取未绑定键
        List<RegistryKey<T>> unboundKeys = this.keyToEntry.entrySet().stream()
                .filter(entry -> entry.getValue() == null || !entry.getValue().hasKeyAndValue())
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(RegistryKey::getValue))
                .toList();
        /*
        // ===================== 新增：打印注册表状态 =====================
        MITENewWorld.LOGGER.info("===== 注册表冻结开始: {} =====", this.key);

        // 1. 打印基本信息
        MITENewWorld.LOGGER.info("注册表键: {}", this.key);
        MITENewWorld.LOGGER.info("原始ID条目数: {}", this.rawIdToEntry.size());
        MITENewWorld.LOGGER.info("已冻结: {}", this.frozen);
        MITENewWorld.LOGGER.info("未绑定键数: {}", unboundKeys.size());
        MITENewWorld.LOGGER.info("侵入式条目: {}", this.intrusiveValueToEntry != null ?
                this.intrusiveValueToEntry.size() : "无");

        // 2. 打印原始ID映射
        MITENewWorld.LOGGER.info("--- 原始ID映射(rawIdToEntry) ---");
        for (int i = 0; i < this.rawIdToEntry.size(); i++) {
            RegistryEntry.Reference<T> entry = this.rawIdToEntry.get(i);
            if (entry != null && entry.value() != null) {
                MITENewWorld.LOGGER.info("[{}] {} -> {}", i,
                        entry.registryKey().getValue(), entry.value());
            } else if (entry != null) {
                MITENewWorld.LOGGER.info("[{}] {} -> <未绑定值>", i,
                        entry.registryKey().getValue());
            } else {
                MITENewWorld.LOGGER.info("[{}] <空槽位>", i);
            }
        }

        // 3. 打印键到条目的映射
        MITENewWorld.LOGGER.info("--- 键到条目映射(keyToEntry) ---");
        this.keyToEntry.forEach((key, entry) -> {
            if (entry != null && entry.value() != null) {
                MITENewWorld.LOGGER.info("{} -> {} (rawId={})",
                        key.getValue(), entry.value(), this.entryToRawId.getInt(entry.value()));
            } else if (entry != null) {
                MITENewWorld.LOGGER.info("{} -> <未绑定值>", key.getValue());
            } else {
                MITENewWorld.LOGGER.info("{} -> <null条目>", key.getValue());
            }
        });

        // 4. 打印未绑定键详情
        if (!unboundKeys.isEmpty()) {
            MITENewWorld.LOGGER.warn("--- 未绑定键 ---");
            for (RegistryKey<T> key : unboundKeys) {
                RegistryEntry.Reference<T> entry = this.keyToEntry.get(key);
                MITENewWorld.LOGGER.warn("  {}: {}", key.getValue(),
                        (entry != null && entry.hasKeyAndValue()) ? "部分绑定" : "完全未绑定");
            }
        }


        MITENewWorld.LOGGER.info("===== 注册表冻结完成 =====");
        // ===================== 状态打印结束 =====================

        // 处理未绑定键（可选）
        if (!unboundKeys.isEmpty()) {
            MITENewWorld.LOGGER.warn("Cleaning {} unbound entries in registry {}",
                    unboundKeys.size(), this.key);
            unboundKeys.forEach(this::safeRemoveEntry);
        }

        // 处理侵入式条目
        if (this.intrusiveValueToEntry != null) {
            if (!this.intrusiveValueToEntry.isEmpty()) {
                MITENewWorld.LOGGER.warn("Clearing {} intrusive holders",
                        this.intrusiveValueToEntry.size());
                this.intrusiveValueToEntry.clear();
            }
            this.intrusiveValueToEntry = null;
        }

        cir.setReturnValue((Registry<T>) this);
    } */
    @Unique
    private void safeRemoveEntry(RegistryKey<T> key) {
        try {
            RegistryEntry.Reference<T> entry = this.keyToEntry.remove(key);
            if (entry != null) {
                this.idToEntry.remove(key.getValue());

                if (entry.value() != null) {
                    this.valueToEntry.remove(entry.value());

                    // 保留原始ID位置，仅标记为null
                    int rawId = this.entryToRawId.removeInt(entry.value());
                    if (rawId >= 0 && rawId < this.rawIdToEntry.size()) {
                        this.rawIdToEntry.set(rawId, null);
                    }
                }
            }
            this.keyToEntryInfo.remove(key);
        } catch (Exception e) {
            MITENewWorld.LOGGER.error("error in {} from registry {}", key, this.key, e);
        }
    }

    @Shadow
    public RegistryKey<? extends Registry<T>> getKey() {
        return null;
    }
    @Inject(method = "iterator", at = @At("HEAD"), cancellable = true)
    public void iterator(CallbackInfoReturnable<Iterator<T>> cir) {
        cir.setReturnValue(Iterators.filter( Iterators.transform( Iterators.filter(this.rawIdToEntry.iterator(), Objects::nonNull), RegistryEntry.Reference::value ), Objects::nonNull ));
    }

    @Inject(method = "streamEntries", at = @At("HEAD"), cancellable = true)
    public void streamEntries(CallbackInfoReturnable<Stream<RegistryEntry.Reference<T>>> cir) {
        cir.setReturnValue(this.rawIdToEntry.stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.value() != null));
    }

}
