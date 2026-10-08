package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.metal.ModToolItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity {

    protected SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        // 手里不是模组的剪刀：不取消，交回原版（进食 / 繁殖）
        if (!(itemStack.getItem() instanceof ModToolItem item) || item.toolType() != ModToolItem.ToolType.SHEARS) {
            return;
        }
        World world = this.getEntityWorld();
        if (!(world instanceof ServerWorld serverWorld)) {
            // 客户端侧只占位，不能有副作用
            cir.setReturnValue(ActionResult.CONSUME);
            return;
        }
        cir.cancel();
        if (this.isShearable()) {
            this.sheared(serverWorld, SoundCategory.PLAYERS, itemStack);
            this.emitGameEvent(GameEvent.SHEAR, player);
            itemStack.damage(15, player, hand);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Shadow
    public void sheared(ServerWorld world2, SoundCategory shearedSoundCategory, ItemStack shears) {

    }

    @Shadow
    public boolean isShearable() {
        return false;
    }
    @Shadow
    public void setSheared(boolean sheared) {
    }




}
