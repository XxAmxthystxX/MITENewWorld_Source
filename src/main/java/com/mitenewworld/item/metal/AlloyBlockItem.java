package com.mitenewworld.item.metal;

import com.mitenewworld.entity.blockentity.AlloyBlockEntity;
import com.mitenewworld.item.component.ModDataComponentTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.Optional;

public class AlloyBlockItem extends BlockItem {

    /**
     * 合金块放置开关：因性能问题暂时关闭。
     * 恢复时改回 true 即可，下方写入方块实体的逻辑保持原样。
     */
    private static final boolean PLACEMENT_ENABLED = false;

    private static final Text PLACEMENT_DISABLED = Text.literal("因性能问题，暂时移除放置功能");

    public AlloyBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static ItemStack fromComponent(Item item, AlloyComponent comp) {
        return AlloyItem.createFromComponent(item, AlloyItem.Form.BLOCK, comp);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        if (!PLACEMENT_ENABLED) {
            if (!context.getWorld().isClient() && context.getPlayer() != null) {
                context.getPlayer().sendMessage(PLACEMENT_DISABLED, true);
            }
            return ActionResult.FAIL;
        }
        ActionResult result = super.place(context);
        if (result.isAccepted() && context.getWorld() instanceof ServerWorld serverWorld
                && serverWorld.getBlockEntity(context.getBlockPos()) instanceof AlloyBlockEntity be) {
            be.setAlloy(context.getStack().get(ModDataComponentTypes.ALLOY_COMPONENT));
        }
        return result;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return AlloyItem.tooltipFor(stack);
    }
}
