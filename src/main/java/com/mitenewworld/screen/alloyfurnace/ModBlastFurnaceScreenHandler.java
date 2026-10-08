package com.mitenewworld.screen.alloyfurnace;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.ModScreenHandlers;

import com.mitenewworld.core.FuelMap;
import com.mitenewworld.entity.blockentity.alloyfurnace.AbstractModBlastFurnaceEntity;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.data.ModBlastFurnaceBlockData;
import com.mitenewworld.data.ModFurnaceBlockData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModBlastFurnaceScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final AbstractModBlastFurnaceEntity blockEntity;
    private final int inputSlotCount;
    private final int tier;
    private Slot fuelSlot;
    private Slot fluxSlot;

    public ModBlastFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, ModBlastFurnaceBlockData data) {
        this(syncId, playerInventory,
                (AbstractModBlastFurnaceEntity) playerInventory.player.getEntityWorld().getBlockEntity(data.pos()),
                new ArrayPropertyDelegate(7), data.tier());
    }

    public ModBlastFurnaceScreenHandler(int syncId, PlayerInventory playerInventory,
                                     @Nullable AbstractModBlastFurnaceEntity entity, PropertyDelegate delegate, int tier) {
        super(ModScreenHandlers.MOD_BAST_FURNACE_SCREEN_HANDLER, syncId);
        int slots = entity != null
                ? entity.getInputSlotCount()
                : AbstractModBlastFurnaceEntity.INPUT_SLOTS[Math.clamp(tier, 1, 5) - 1];
        this.inventory = entity != null ? entity : new SimpleInventory(slots + 2);
        this.blockEntity = entity;
        this.propertyDelegate = delegate;
        this.inputSlotCount = slots;
        this.tier = tier;
        inventory.onOpen(playerInventory.player);

        List<int[]> offsets = inputSlotOffsets(tier);
        int cx = 60, cy = 59;
        for (int i = 0; i < inputSlotCount; i++) {
            int[] o = offsets.get(i);
            this.addSlot(new InputSlot(inventory, i, cx + o[0] * 18, cy + o[1] * 18, delegate));
        }

        int fuelX = 142, fuelY = 105;
        this.fuelSlot = new Slot(inventory, slots, fuelX, fuelY) {
            @Override public boolean canInsert(ItemStack stack) {
                return FuelMap.accepts(tier, stack.getItem()) || FuelMap.isExtinguisher(stack.getItem());
            }
        };
        this.addSlot(this.fuelSlot);
        // 析金粉槽（有析金粉 = 析出模式），只接受析金粉
        this.fluxSlot = new Slot(inventory, slots + 1, 142, 15) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.REFINING_POWDER);
            }
        };
        this.addSlot(this.fluxSlot);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    /** 1:中心 / 2:十字 / 3:3x3 / 4:3x3+四方向 / 5:5x5去四角 */
    private static List<int[]> inputSlotOffsets(int tier) {
        List<int[]> list = new ArrayList<>();
        switch (tier) {
            case 1 -> list.add(new int[]{0, 0});
            case 2 -> {
                list.add(new int[]{0, 0});
                list.add(new int[]{-1, 0}); list.add(new int[]{1, 0});
                list.add(new int[]{0, -1}); list.add(new int[]{0, 1});
            }
            case 3 -> {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        list.add(new int[]{dx, dy});
                    }
                }
            }
            case 4 -> {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        list.add(new int[]{dx, dy});
                    }
                }
                list.add(new int[]{-2, 0}); list.add(new int[]{2, 0});
                list.add(new int[]{0, -2}); list.add(new int[]{0, 2});
            }
            case 5 -> {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dx = -2; dx <= 2; dx++) {
                        if (Math.abs(dx) == 2 && Math.abs(dy) == 2) {
                            continue;
                        }
                        list.add(new int[]{dx, dy});
                    }
                }
            }
        }
        return list;
    }

    private void addPlayerInventory(PlayerInventory inv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 7 + j * 18, 137 + i * 18));
            }
        }
    }
    private void addPlayerHotbar(PlayerInventory inv) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 7 + i * 18, 195));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            result = stack.copy();

            int fuelIndex   = inputSlotCount;
            int fluxIndex   = inputSlotCount + 1;
            int playerStart = inputSlotCount + 2;

            if (slotIndex == fluxIndex) {
                if (!insertItem(stack, playerStart, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(stack, result);
            } else if (slotIndex < inputSlotCount || slotIndex == fuelIndex) {
                if (!insertItem(stack, playerStart, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (AbstractModBlastFurnaceEntity.isValidInput(stack)) {
                    if (!insertItem(stack, 0, inputSlotCount, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (FuelMap.accepts(tier, stack.getItem()) || FuelMap.isExtinguisher(stack.getItem())) {
                    if (!insertItem(stack, fuelIndex, fuelIndex + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < playerStart + 27) {
                    if (!insertItem(stack, playerStart + 27, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!insertItem(stack, playerStart, playerStart + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, stack);
        }
        return result;
    }

    @Override public boolean canUse(PlayerEntity player) { return inventory.canPlayerUse(player); }
    public PropertyDelegate getPropertyDelegate() { return propertyDelegate; }
    public AbstractModBlastFurnaceEntity getBlockEntity() { return blockEntity; }
    public int getInputSlotCount() { return inputSlotCount; }
    public int getTier() { return tier; }
    public Slot getFuelSlot() { return fuelSlot; }
    public Slot getFluxSlot() { return fluxSlot; }
    public int getFuelSlotIndex()   { return inputSlotCount; }
    public int getOutputSlotIndex() { return inputSlotCount + 1; }

    private static class InputSlot extends Slot {
        private final PropertyDelegate delegate;
        public InputSlot(Inventory inv, int index, int x, int y, PropertyDelegate d) {
            super(inv, index, x, y);
            this.delegate = d;
        }
        @Override public boolean canInsert(ItemStack stack) {
            if (delegate.get(0) != 0) {
                return false;
            }
            return AbstractModBlastFurnaceEntity.isValidInput(stack);
        }
        @Override public boolean canTakeItems(PlayerEntity player) { return delegate.get(0) == 0; }
    }
}
