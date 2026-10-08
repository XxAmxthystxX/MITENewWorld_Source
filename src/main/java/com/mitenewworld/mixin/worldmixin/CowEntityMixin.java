package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.cover.ItemsCover;
import com.mitenewworld.item.metal.ModBucketItems;
import com.mitenewworld.item.ModFoodItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractCowEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends AbstractCowEntity {
    @Unique
    private static final TrackedData<Integer> MILK_LEVEL_NET = DataTracker.registerData(CowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> MILK_CD_NET = DataTracker.registerData(CowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private int MilkLevel = 4;
    @Unique
    private int MilkCD = 0;

    public CowEntityMixin(EntityType<? extends AbstractCowEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(MILK_LEVEL_NET, 4);
        builder.add(MILK_CD_NET, 0);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getEntityWorld().isClient()) {
            return;
        }
        if (MilkLevel >= 4) {
            return;
        }
        // 冷却只存在服务端字段里，不再每 tick 写 tracker（那会每 tick 触发一次实体数据同步）；
        // 只有牛奶等级真正变化时才同步一次
        MilkCD++;
        if (MilkCD >= 12000) {
            MilkLevel++;
            MilkCD = 0;
            this.dataTracker.set(MILK_LEVEL_NET, MilkLevel);
        }
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    public void FixedWriteCustomData(WriteView view, CallbackInfo ci) {
        view.putInt("MilkLevel", this.MilkLevel);
        view.putInt("MilkCD", this.MilkCD);
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void FixedReadCustomData(ReadView view, CallbackInfo ci) {
        if (view.contains("MilkLevel")) {
            this.dataTracker.set(MILK_LEVEL_NET, view.getInt("MilkLevel" , 0));
            this.MilkLevel = view.getInt("MilkLevel" , 0);
        }
        if (view.contains("MilkCD")) {
            this.dataTracker.set(MILK_CD_NET, view.getInt("MilkCD" , 100));
            this.MilkCD = view.getInt("MilkCD" , 100);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(!this.isBaby()){
            if (itemStack.getItem() instanceof ModBucketItems item && MilkLevel == 4 && item.fluid == Fluids.EMPTY) {
                ItemStack itemStack2 = ItemUsage.exchangeStack(
                        itemStack,
                        player,
                        switch (item.bucketLevel) {
                            case COPPER -> new ItemStack(ModFoodItems.MILK_COPPER_BUCKET);
                            case SILVER -> new ItemStack(ModFoodItems.MILK_SILVER_BUCKET);
                            case GOLD -> new ItemStack(ModFoodItems.MILK_GOLD_BUCKET);
                            case IRON -> new ItemStack(ModFoodItems.MILK_IRON_BUCKET);
                            case MITHRIL -> new ItemStack(ModFoodItems.MILK_MITHRIL_BUCKET);
                            case ANCIENT_METAL -> new ItemStack(ModFoodItems.MILK_ANCIENT_METAL_BUCKET);
                            case ADAMANTIUM -> new ItemStack(ModFoodItems.MILK_ADAMANTIUM_BUCKET);
                        }
                );
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                player.setStackInHand(hand, itemStack2);
                MilkLevel = 0;
                dataTracker.set(MILK_LEVEL_NET, MilkLevel);
                return ActionResult.SUCCESS;
            } else if (itemStack.isOf(ItemsCover.BOWL) && MilkLevel > 0) {
                ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, player, new ItemStack(ModFoodItems.BOWL_OF_MILK));
                player.setStackInHand(hand, itemStack3);
                MilkLevel = MilkLevel - 1;
                dataTracker.set(MILK_LEVEL_NET, MilkLevel);
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                return ActionResult.SUCCESS;
            } else {
                return super.interactMob(player, hand);
            }
        } else {
            return super.interactMob(player, hand);
        }
    }
}
