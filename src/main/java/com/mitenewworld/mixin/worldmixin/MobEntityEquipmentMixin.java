package com.mitenewworld.mixin.worldmixin;

import com.mitenewworld.entity.mob.MobEquipment;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 怪物自然生成完成后，按 MITE 风格给它配一件本模组的工具 / 武器。
 * 注入点选在 MobEntity.initialize 的 TAIL：此时子类的 initEquipment（骷髅装弓等）已经跑完。
 */
@Mixin(MobEntity.class)
public class MobEntityEquipmentMixin {

    @Inject(method = "initialize", at = @At("TAIL"))
    private void mitenewworld$equipModTools(ServerWorldAccess world, LocalDifficulty difficulty,
                                            SpawnReason spawnReason, EntityData entityData,
                                            CallbackInfoReturnable<EntityData> cir) {
        MobEntity mob = (MobEntity) (Object) this;
        MobEquipment.equip(mob, mob.getRandom(), difficulty, spawnReason);
    }
}
