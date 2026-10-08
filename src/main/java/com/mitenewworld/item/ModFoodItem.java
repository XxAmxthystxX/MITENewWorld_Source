package com.mitenewworld.item;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.tooltip.FoodTipComponent;
import com.mitenewworld.item.component.ModTooltipData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModFoodItem extends BlockItem {

    public ModFoodItem(Block block, Settings settings) {
        super(block, settings);
        MITENewWorld.LOGGER.info("ModFoodItem created : {}", block);
    }

    /** 纯食物版本 */
    public ModFoodItem(Settings settings) {
        super(Blocks.AIR, settings);
    }

    // ============================================================
    //  Tooltip
    // ============================================================

    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (food == null) {
            return Optional.empty();
        }
        return Optional.of(new ModTooltipData( FoodTipComponent.of(food.nutrition(), food.saturation())));
    }

    // ============================================================
    //  放置
    // ============================================================

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (getBlock() == Blocks.AIR) {
            return ActionResult.PASS;
        }
        return super.useOnBlock(context);
    }

    /** 未完工，预留 */
    @SuppressWarnings("unused")
    private ActionResult tryPlaceBlock(ItemUsageContext context, BlockStateComponent stateComponent) {
        ItemPlacementContext placementContext = new ItemPlacementContext(context);
        BlockState state = Blocks.AIR.getDefaultState();
        Block block = state.getBlock();
        if (!block.isEnabled(context.getWorld().getEnabledFeatures())) {
            return ActionResult.FAIL;
        }
        if (!context.getWorld().canPlace(state, placementContext.getBlockPos(),
                ShapeContext.ofPlacement(context.getPlayer()))) {
            return ActionResult.FAIL;
        }
        if (!context.getWorld().setBlockState(placementContext.getBlockPos(), state,
                Block.NOTIFY_ALL_AND_REDRAW)) {
            return ActionResult.FAIL;
        }

        var blockEntityData = context.getStack().get(DataComponentTypes.BLOCK_ENTITY_DATA);
        if (blockEntityData != null) {
            BlockEntity blockEntity = context.getWorld()
                    .getBlockEntity(placementContext.getBlockPos());
            if (blockEntity != null) {
                blockEntityData.applyToBlockEntity(blockEntity, context.getWorld().getRegistryManager());
            }
        }

        BlockPos pos = placementContext.getBlockPos();
        BlockSoundGroup soundGroup = state.getSoundGroup();
        context.getWorld().playSound(context.getPlayer(), pos,
                soundGroup.getPlaceSound(), SoundCategory.BLOCKS,
                (soundGroup.getVolume() + 1.0F) / 2.0F,
                soundGroup.getPitch() * 0.8F);
        context.getWorld().emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(context.getPlayer(), state));

        if (context.getPlayer() == null
                || !context.getPlayer().getAbilities().creativeMode) {
            context.getStack().decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}