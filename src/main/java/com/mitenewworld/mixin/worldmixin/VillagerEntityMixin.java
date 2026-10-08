package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {
    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        cir.cancel();
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.VILLAGER_SPAWN_EGG) || !this.isAlive() || this.hasCustomer() || this.isSleeping()) {
            cir.setReturnValue( super.interactMob(player, hand));
        } else if (this.isBaby()) {
            this.sayNo();
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (player.experienceLevel >= 35){
            if (!this.getEntityWorld().isClient()) {
                boolean bl = this.getOffers().isEmpty();
                if (hand == Hand.MAIN_HAND) {
                    if (bl) {
                        this.sayNo();
                    }

                    player.incrementStat(Stats.TALKED_TO_VILLAGER);
                }

                if (bl) {
                    cir.setReturnValue(ActionResult.CONSUME);
                    return;
                }

                this.beginTradeWith(player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            this.sayNo();
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Shadow
    private void beginTradeWith(PlayerEntity player) {
    }

    @Shadow
    private void sayNo() {
    }
}
