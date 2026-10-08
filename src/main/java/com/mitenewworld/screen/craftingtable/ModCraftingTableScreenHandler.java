package com.mitenewworld.screen.craftingtable;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.ModScreenHandlers;

import com.mitenewworld.core.ImplementedInventory;
import com.mitenewworld.entity.blockentity.craftingtable.AbstractModCraftingTableEntity;
import com.mitenewworld.data.ModCraftingTableBlockData;
import com.mitenewworld.recipe.ModCraftingRecipe;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;


public class ModCraftingTableScreenHandler extends AbstractRecipeScreenHandler {

    private final ImplementedInventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final AbstractModCraftingTableEntity blockEntity;
    private final ScreenHandlerContext context = ScreenHandlerContext.EMPTY;
    private PlayerEntity player;
    private int blockCraftLevel;


    public ModCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, ModCraftingTableBlockData data) {
        this(syncId, playerInventory, playerInventory.player.getEntityWorld().getBlockEntity(data.pos()), new ArrayPropertyDelegate(5));
    }

    public ModCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.MOD_CRAFTING_TABLE_SCREEN_HANDLER, syncId);
        checkSize((Inventory) blockEntity , 10);
        this.inventory = (ImplementedInventory)blockEntity;
        inventory.onOpen(playerInventory.player);
        player = playerInventory.player;
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = (AbstractModCraftingTableEntity) blockEntity;
        this.blockCraftLevel = this.blockEntity.getCraftLevel();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        this.addSlot(new Slot(inventory, 9, 124, 35));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraftByPlayer(itemStack2, player));
                if (!this.insertItem(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot >= 10 && slot < 46) {
                if (!this.insertItem(itemStack2, 1, 10, false)) {
                    if (slot < 37) {
                        if (!this.insertItem(itemStack2, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 10, 46, false)) {
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
            if (slot == 0) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.canUse(player) &&  (propertyDelegate.get(2) == 3 ) && id == 0) {
            isCrafting();
            return true;
        }
        return false;
    }
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        notCrafting();
    }
    public void isCrafting() {
         propertyDelegate.set(3,1);
    }
    public void notCrafting() {
         propertyDelegate.set(3,0);
    }


    public PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }


    @Override
    public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
        try {
            List<Slot> list = this.slots.subList(0 , 9);
            return InputSlotFiller.fill(new InputSlotFiller.Handler<>() {

                @Override
                public void populateRecipeFinder(RecipeFinder finder) {
                    ModCraftingTableScreenHandler.this.populateRecipeFinder(finder);
                }

                @Override
                public void clear() {
                    ModCraftingTableScreenHandler.this.inventory.clear();
                }

                @Override
                public boolean matches(RecipeEntry<ModCraftingRecipe> entry) {
                    return entry.value().matches(CraftingRecipeInput.createPositioned(3, 3, blockEntity.getItems().subList(0, 9)).input(), ModCraftingTableScreenHandler.this.player.getEntityWorld());
                }
            }, 3, 3, list, list, inventory, (RecipeEntry<ModCraftingRecipe>) recipe, craftAll, creative);
        } catch (Exception e) {
            return PostFillAction.NOTHING;
        }
    }


    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        for (int i = 0; i < 9; i++) {
            finder.addInputIfUsable(this.inventory.getStack(i));
        }
    }
    @Override
    public RecipeBookType getCategory() {
        return RecipeBookType.CRAFTING;
    }

    public int getWidth() {
        return 3;
    }
    public int getHeight() {
        return 3;
    }
    public Slot getOutputSlot() {
        return getSlot(9);
    }
    public List<Slot> getInputSlots() {
        return slots.subList(0, 9);
    }
    public int getBlockCraftLevel() {
        return blockCraftLevel;
    }

}
