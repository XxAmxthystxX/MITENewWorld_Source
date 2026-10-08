package com.mitenewworld.entity.blockentity.craftingtable;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.core.ImplementedInventory;
import com.mitenewworld.data.ModCraftingTableBlockData;
import com.mitenewworld.recipe.ModCraftingRecipe;
import com.mitenewworld.screen.craftingtable.ModCraftingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractModCraftingTableEntity extends BlockEntity implements ExtendedScreenHandlerFactory<ModCraftingTableBlockData> , ImplementedInventory {
    protected @Nullable RecipeEntry<ModCraftingRecipe> Recipe;
    protected int lastInputHash = 0;
    protected final DefaultedList<ItemStack> inventory;
    protected final PropertyDelegate propertyDelegate;
    protected int progress = 0;
    protected int maxprogress = 100;
    protected int craftstate = 0;
    protected int craftingstate = 0;
    protected final int craftLevel;

    public AbstractModCraftingTableEntity(BlockEntityType<?> type, BlockPos pos, BlockState state , int CraftLevel) {
        super(type, pos, state);
        this.craftLevel = CraftLevel;
        this.inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
        this.propertyDelegate = createPropertyDelegate();
    }


    private PropertyDelegate createPropertyDelegate() {
        return new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxprogress;
                    case 2 -> craftstate;
                    case 3 -> craftingstate;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxprogress = value;
                    case 2 -> craftstate = value;
                    case 3 -> craftingstate = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }
    @Override
    public ModCraftingTableBlockData getScreenOpeningData(ServerPlayerEntity player) {
        return new ModCraftingTableBlockData(pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.modcraftingtable");
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view , this.inventory );
        view.putInt("modcraftingtable_progress",progress);
    }
    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view , this.inventory  );
        progress = view.getInt("modcraftingtable_progress" , 0);
    }

    public int getMaxCountPerStack(){
        return 16;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ModCraftingTableScreenHandler(syncId, playerInventory, this , propertyDelegate);
    }
    private int computeInputHash() {
        int hash = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.get(i);
            hash = 31 * hash + Objects.hash(stack.getItem(), stack.getCount());
        }
        return hash;
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }
        if (!isOutputSlotAvailable()){
            this.craftstate = 1 ;
        } else if (isOutputSlotAvailable() && hasRecipe() && !recipelevel()) {
            this.craftstate = 2 ;
        } else if (isOutputSlotAvailable() && hasRecipe() && recipelevel()) {
            this.craftstate = 3 ;
        } else {
            this.craftstate = 0 ;
        }
        if (craftingstate == 1 && craftstate == 3) {
            increaseCraftProgress();
            if (hasCraftingFinished()) {
                craftItem();
                removeStack(inventory.subList(0 , 9) , world , pos);
                resetProgress();
            }
        } else {
            craftingstate = 0;
            resetProgress();
        }
        markDirty(world, pos, state);
    }

    private void resetProgress() {
        this.progress = 0;
    }
    public boolean recipelevel(){
        Optional<RecipeEntry<ModCraftingRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent() && recipe.get().value().getCraftLevel() <= craftLevel;
    }


    public boolean hasRecipe() {
        Optional<RecipeEntry<ModCraftingRecipe>> recipe = getCurrentRecipe();
        recipe.ifPresent(modCraftingTableRecipeRecipeEntry -> this.maxprogress = modCraftingTableRecipeRecipeEntry.value().getCraftTick());
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(recipe.get().value().craft(null , null)) &&
                canInsertItemIntoOutputSlot(recipe.get().value().craft(null , null).getItem());
    }

    public void removeStack(List<ItemStack> inputStacks, World world , BlockPos pos) {
        for (ItemStack itemStack : inputStacks) {
            itemStack.decrement(1);
            if (!itemStack.getRecipeRemainder().isEmpty()) {
                ItemStack remainder = itemStack.getRecipeRemainder().copy();
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), remainder));
            }
        }
    }
    public void craftItem() {
        Optional<RecipeEntry<ModCraftingRecipe>> recipe = getCurrentRecipe();
        recipe.ifPresent(modCraftingTableRecipeRecipeEntry ->
                this.setStack( 9 , new ItemStack(modCraftingTableRecipeRecipeEntry.value().craft(null , null).getItem(),
                getStack(9).getCount() + modCraftingTableRecipeRecipeEntry.value().craft(null , null).getCount())));
    }

    public Optional<RecipeEntry<ModCraftingRecipe>> getCurrentRecipe() {
        int currentHash = computeInputHash();
        if (currentHash == lastInputHash && Recipe != null) {
            return Optional.of(Recipe);
        }
        this.progress = 0;
        CraftingRecipeInput input = CraftingRecipeInput.createPositioned(3 , 3, inventory.subList(0, 9)).input();
        Optional<RecipeEntry<ModCraftingRecipe>> newRecipe =
                getWorld().getServer().getRecipeManager().getFirstMatch(ModCraftingRecipe.Type.INSTANCE, input, getWorld());
        lastInputHash = currentHash;
        Recipe = newRecipe.orElse(null);
        return newRecipe;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxprogress;
    }

    private void increaseCraftProgress() {
        progress+=craftLevel;
    }


    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(9).getCount() + result.getCount() <= MathHelper.clamp(result.getMaxCount() , 1 , 16);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(9).getItem() == item ||
                this.getStack(9).isEmpty();

    }

    private boolean isOutputSlotAvailable() {
        return this.getStack(9).isEmpty() ||
                this.getStack(9).getCount() < 16;
    }
    public int getCraftLevel(){
        return craftLevel;
    }

}
