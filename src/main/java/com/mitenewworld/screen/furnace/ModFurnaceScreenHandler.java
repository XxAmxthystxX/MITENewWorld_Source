package com.mitenewworld.screen.furnace;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.ModScreenHandlers;

import com.mitenewworld.core.FuelMap;
import com.mitenewworld.entity.blockentity.furnace.AbstractModFurnaceEntity;
import com.mitenewworld.data.ModFurnaceBlockData;
import com.mitenewworld.recipe.ModFurnaceRecipe;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class ModFurnaceScreenHandler extends AbstractRecipeScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final AbstractModFurnaceEntity blockEntity ;
    private PlayerEntity player;
    private final RecipePropertySet recipePropertySet ;


    public ModFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, ModFurnaceBlockData data) {
        this(syncId, playerInventory, playerInventory.player.getEntityWorld().getBlockEntity(data.pos()), new ArrayPropertyDelegate(5));
    }
    public ModFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.MOD_FURNACE_SCREEN_HANDLER , syncId );
        this.inventory = (Inventory)blockEntity;
        inventory.onOpen(playerInventory.player);
        this.player = playerInventory.player;
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = (AbstractModFurnaceEntity) blockEntity;
        int fuelLevel = this.blockEntity != null ? this.blockEntity.getFurnaceLevel() : 1;
        this.addSlot(new Slot(inventory, 1, 56, 17));
        this.addSlot(new Slot(inventory, 0, 56, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return FuelMap.accepts(fuelLevel, stack.getItem()) || FuelMap.isExtinguisher(stack.getItem());
            }
        });
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35));
        this.recipePropertySet = player.getEntityWorld().getRecipeManager().getPropertySet(RecipePropertySet.FURNACE_INPUT);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }


    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot != 1 && slot != 0) {
                if (this.isSmeltable(itemStack2)) {
                    if (!this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemStack2)) {
                    if (!this.insertItem(itemStack2, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 3 && slot < 30) {
                    if (!this.insertItem(itemStack2, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 30 && slot < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    private boolean isFuel(ItemStack itemStack2) {
        if (FuelMap.isExtinguisher(itemStack2.getItem())) {
            return true;
        }
        int level = this.blockEntity != null ? this.blockEntity.getFurnaceLevel() : 1;
        return FuelMap.accepts(level, itemStack2.getItem());
    }

    protected boolean isSmeltable(ItemStack itemStack) {
       return this.recipePropertySet.canUse(itemStack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }



    @Override
    public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
        final List<Slot> list = List.of(this.getSlot(0), this.getSlot(2));
        return InputSlotFiller.fill(new InputSlotFiller.Handler<>() {
            @Override
            public void populateRecipeFinder(RecipeFinder finder) {
                ModFurnaceScreenHandler.this.populateRecipeFinder(finder);
            }

            @Override
            public void clear() {
                list.forEach(slot -> slot.setStackNoCallbacks(ItemStack.EMPTY));
            }

            @Override
            public boolean matches(RecipeEntry<ModFurnaceRecipe> entry) {
                return entry.value().matches(new SingleStackRecipeInput(ModFurnaceScreenHandler.this.inventory.getStack(0)), world);
            }
        }, 1, 1, List.of(this.getSlot(0)), list, inventory, (RecipeEntry<ModFurnaceRecipe>)recipe, craftAll, creative);
    }


    @Override
    public void populateRecipeFinder(RecipeFinder finder) {

    }

    @Override
    public RecipeBookType getCategory() {
        return RecipeBookType.FURNACE;
    }

    public Slot getInputSlot() {
        return this.slots.get(1);
    }
    public Slot getFuelSlot() {
        return this.slots.getFirst();
    }
    public Slot getOutputSlot() {
        return this.slots.get(2);
    }


}
