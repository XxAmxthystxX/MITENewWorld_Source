package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.ModBucketLevel;
import com.mitenewworld.cover.ItemsCover;
import com.mitenewworld.registry.ModItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ModBucketItems extends Item implements FluidModificationItem {

    public final Fluid fluid;
    public final ModBucketLevel bucketLevel;

    public ModBucketItems(Fluid fluid , ModBucketLevel bucketLevel , Settings settings ) {
        super(settings);
        this.fluid = fluid;
        this.bucketLevel = bucketLevel;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, this.fluid == Fluids.EMPTY ? RaycastContext.FluidHandling.ANY: RaycastContext.FluidHandling.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return ActionResult.PASS;
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);
            if (!world.canEntityModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
                return ActionResult.FAIL;
            } else if (this.fluid == Fluids.EMPTY) {
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof FluidDrainable fluidDrainable) {
                    ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, user,getFilledStack(itemStack, blockState));
                    if (!itemStack2.isEmpty()) {
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        fluidDrainable.getBucketFillSound().ifPresent(sound -> user.playSound(sound, 1.0F, 1.0F));
                        world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                        ItemUsage.exchangeStack(itemStack, user, itemStack2);
                        if (!world.isClient()) {
                            Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity)user, itemStack2);
                        }

                        return ActionResult.SUCCESS;
                    }
                }

                return ActionResult.FAIL;
            } else {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos blockPos3 = blockState.getBlock() instanceof FluidFillable && this.fluid == Fluids.WATER ? blockPos : blockPos2;
                if (blockState.getBlock() == Blocks.FARMLAND) {
                    world.setBlockState(blockPos, blockState.with(FarmlandBlock.MOISTURE, 7), 3);
                    this.onEmptied(user, world, itemStack, blockPos);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    ItemUsage.exchangeStack(itemStack, user, getemptiedStack(itemStack, user));
                    return ActionResult.SUCCESS;
                } else if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                    this.onEmptied(user, world, itemStack, blockPos3);
                    if (user instanceof ServerPlayerEntity) {
                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos3, itemStack);
                    }
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    ItemUsage.exchangeStack(itemStack, user, getemptiedStack(itemStack, user));
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.FAIL;
                }
            }
        }
    }

    @Override
    public boolean placeFluid(@Nullable LivingEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(this.fluid instanceof FlowableFluid flowableFluid)) {
            return false;
        } else {
            Block block;
            boolean bl;
            BlockState blockState;
            boolean var10000;
            label82: {
                blockState = world.getBlockState(pos);
                block = blockState.getBlock();
                bl = blockState.canBucketPlace(this.fluid);
                label70:
                if (!blockState.isAir() && !bl) {
                    if (block instanceof FluidFillable fluidFillable && fluidFillable.canFillWithFluid(player, world, pos, blockState, this.fluid)) {
                        break label70;
                    }

                    var10000 = false;
                    break label82;
                }

                var10000 = true;
            }

            boolean bl2 = var10000;
            if (!bl2) {
                return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
            } else if (world.getDimension().ultrawarm() && this.fluid == Fluids.WATER) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                world.playSound(
                        player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F
                );

                for (int l = 0; l < 8; l++) {
                    world.addParticleClient(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0, 0.0, 0.0);
                }

                return true;
            } else {
                if (block instanceof FluidFillable fluidFillable && this.fluid == Fluids.WATER) {
                    fluidFillable.tryFillWithFluid(world, pos, blockState, flowableFluid.getStill(false));
                    this.playEmptyingSound(player, world, pos);
                    return true;
                }

                if (!world.isClient() && bl && blockState.getBlock() != Blocks.WATER && blockState.getBlock() != Blocks.LAVA) {
                    world.breakBlock(pos, true);
                }

                if (!world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW) && !blockState.getFluidState().isStill()) {
                    return false;
                } else {
                    this.playEmptyingSound(player, world, pos);
                    return true;
                }
            }
        }
    }

    public ItemStack getemptiedStack(ItemStack stack, PlayerEntity player) {
        if(player.isInCreativeMode()){
            return stack;
        } else {
            switch (this.bucketLevel) {
                case COPPER -> stack = new ItemStack(ModItems.COPPER_BUCKET);
                case SILVER -> stack = new ItemStack(ModItems.SILVER_BUCKET);
                case GOLD -> stack = new ItemStack(ModItems.GOLD_BUCKET);
                case IRON -> stack = new ItemStack(ModItems.IRON_BUCKET);
                case MITHRIL -> stack = new ItemStack(ModItems.MITHRIL_BUCKET);
                case ANCIENT_METAL -> stack = new ItemStack(ModItems.ANCIENT_METAL_BUCKET);
                case ADAMANTIUM -> stack = new ItemStack(ModItems.ADAMANTIUM_BUCKET);
                default -> stack = new ItemStack(ItemsCover.BUCKET);
            }
        }

        return stack ;
    }

    public ItemStack getFilledStack(ItemStack stack, BlockState state) {
       if(state.getBlock() == Blocks.WATER) {
           switch (this.bucketLevel) {
               case COPPER -> stack = new ItemStack(ModItems.WATER_COPPER_BUCKET);
               case SILVER -> stack = new ItemStack(ModItems.WATER_SILVER_BUCKET);
               case GOLD -> stack = new ItemStack(ModItems.WATER_GOLD_BUCKET);
               case IRON -> stack = new ItemStack(ModItems.WATER_IRON_BUCKET);
               case MITHRIL -> stack = new ItemStack(ModItems.WATER_MITHRIL_BUCKET);
               case ANCIENT_METAL -> stack = new ItemStack(ModItems.WATER_ANCIENT_METAL_BUCKET);
               case ADAMANTIUM -> stack = new ItemStack(ModItems.WATER_ADAMANTIUM_BUCKET);
               default -> stack = new ItemStack(ItemsCover.BUCKET);
           }
       } else if (state.getBlock() == Blocks.LAVA) {
           switch (this.bucketLevel) {
               case COPPER -> stack = new ItemStack(ModItems.LAVA_COPPER_BUCKET);
               case SILVER -> stack = new ItemStack(ModItems.LAVA_SILVER_BUCKET);
               case GOLD -> stack = new ItemStack(ModItems.LAVA_GOLD_BUCKET);
               case IRON -> stack = new ItemStack(ModItems.LAVA_IRON_BUCKET);
               case MITHRIL -> stack = new ItemStack(ModItems.LAVA_MITHRIL_BUCKET);
               case ANCIENT_METAL -> stack = new ItemStack(ModItems.LAVA_ANCIENT_METAL_BUCKET);
               case ADAMANTIUM -> stack = new ItemStack(ModItems.LAVA_ADAMANTIUM_BUCKET);
           }
       } else {
           stack = new ItemStack(ItemsCover.BUCKET);
       }
       return stack;
    }
    protected void playEmptyingSound(@Nullable LivingEntity player, WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = this.fluid.matchesType(Fluids.FLOWING_LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

}
