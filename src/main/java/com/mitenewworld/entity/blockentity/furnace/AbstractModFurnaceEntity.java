package com.mitenewworld.entity.blockentity.furnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.block.furnace.AbstractModFurnaceBlock;
import com.mitenewworld.core.FuelMap;
import com.mitenewworld.core.ImplementedInventory;
import com.mitenewworld.data.ModFurnaceBlockData;
import com.mitenewworld.recipe.ModFurnaceRecipe;
import com.mitenewworld.screen.furnace.ModFurnaceScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
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

public abstract class AbstractModFurnaceEntity extends BlockEntity implements ExtendedScreenHandlerFactory<ModFurnaceBlockData>, ImplementedInventory {
    protected @Nullable RecipeEntry<ModFurnaceRecipe> Recipe;
    protected DefaultedList<ItemStack> Inventory;
    protected PropertyDelegate propertyDelegate;
    protected int MaxBurnTime = 10;
    protected int BurnTime = 0;
    protected int FuelTime = 0;
    protected int MaxFuelTime = 1600;
    protected final int Furnacelevel ;
    protected int ItemFuelTime = 0;
    protected int LastHash = 0;
    protected int CraftState = 1;

    public AbstractModFurnaceEntity(BlockEntityType<?> type , BlockPos pos , BlockState state , int maxFuelTime , int furnaceLevel) {
        super(type, pos , state);
        this.MaxFuelTime = maxFuelTime;
        this.Furnacelevel = furnaceLevel;
        this.Inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                 return switch (index) {
                    case 0 ->  FuelTime;
                    case 1 ->  BurnTime;
                    case 2 ->  MaxBurnTime;
                    case 3 ->  MaxFuelTime;
                    case 4 ->  CraftState;
                    default -> 0;
                 };
            }

            @Override
            public void set(int index, int value) {
                 switch (index) {
                    case 0 ->  FuelTime = value;
                    case 1 ->  BurnTime = value;
                    case 2 ->  MaxBurnTime = value;
                    case 3 ->  MaxFuelTime = value;
                    case 4 ->  CraftState = value;
                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }



    @Override
    public ModFurnaceBlockData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new ModFurnaceBlockData(pos);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ModFurnaceScreenHandler(syncId , playerInventory , this , propertyDelegate);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.Inventory;
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view ,this.Inventory );
        view.putInt("modfurnace_burn_progress",BurnTime);
        view.putInt("modfurnace_fuel_progress",FuelTime);
        view.putInt("modfurnace_max_burn_time",MaxBurnTime);
    }
    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view , this.Inventory );
        BurnTime = view.getInt("modfurnace_burn_progress" , 0);
        FuelTime = view.getInt("modfurnace_fuel_progress" , 0);
        MaxBurnTime = view.getInt("modfurnace_max_burn_time" , 0);
    }


    @Override
    public Text getDisplayName() {
        return Text.translatable("container.mod_furnace");
    }

    @Override
    public int getMaxCountPerStack(){
        return 16;
    }

    public void tick(World world ,BlockPos pos ,BlockState state) {
        if (world.isClient()) {
            return;
        }
        this.UpdateForItemChange();
        if (this.tryExtinguish(world, pos, state)) {
            return;
        }
        if (this.FuelTime > 0) {
            this.FuelTime--;
            if (hasRecipe() && !recipeLevel()){
                this.CraftState = 0;
            } else if (hasRecipe() && recipeLevel() && canCraft() ) {
                this.CraftState = 1;
                this.BurnTime++;
                if (this.BurnTime >= this.MaxBurnTime) {
                    reset();
                    this.craftItem();
                }
            }
            if (canFuel() && this.FuelTime == 0) {
                this.tryBurn(world , pos);
            }
            state = state.with(AbstractModFurnaceBlock.LIT, this.FuelTime > 0);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }

        markDirty(world, pos, state);
    }

    public void reset() {
        this.BurnTime = 0 ;
    }


    public boolean recipeLevel() {
        Optional<RecipeEntry<ModFurnaceRecipe>> recipe = Optional.ofNullable(Recipe);
        return recipe.isPresent() && recipe.get().value().getFuellevel() <= this.Furnacelevel;

    }
    public void craftItem() {
        Optional<RecipeEntry<ModFurnaceRecipe>> recipe = Optional.ofNullable(Recipe);
        ItemStack itemStack = recipe.get().value().craft(null , null);
        this.setStack(2, new ItemStack(itemStack.getItem(), getStack(2).getCount() + itemStack.getCount()));
        removeStack(Inventory , 1 , 1);
    }

    public void removeStack(List<ItemStack> inputStacks, int count , int slot) {
        inputStacks.get(slot).decrement(count);
    }
    public boolean canFuel() {
        return !this.Inventory.getFirst().isEmpty();
    }

    public void tryBurn(World world, BlockPos pos) {
        if (this.ItemFuelTime <= 0) {
            return;
        }
        this.FuelTime = MathHelper.clamp(this.FuelTime + ItemFuelTime, 0, this.MaxFuelTime);
        ItemStack itemStack = this.Inventory.getFirst().getRecipeRemainder();
        if (!itemStack.isEmpty()){
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
        }
        removeStack(Inventory, 1 , 0);
    }

    /** 水碗/水桶：放入燃料槽后熄灭燃料值并返还空容器 */
    public boolean tryExtinguish(World world, BlockPos pos, BlockState state) {
        if (this.FuelTime <= 0) {
            return false;
        }
        ItemStack fuelStack = this.Inventory.getFirst();
        if (fuelStack.isEmpty() || !FuelMap.isExtinguisher(fuelStack.getItem())) {
            return false;
        }
        this.FuelTime = 0;
        ItemStack remainder = fuelStack.getRecipeRemainder();
        if (!remainder.isEmpty()) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remainder));
        }
        removeStack(Inventory, 1, 0);
        world.setBlockState(pos, state.with(AbstractModFurnaceBlock.LIT, false), Block.NOTIFY_ALL);
        markDirty(world, pos, state);
        return true;
    }

    public int getFurnaceLevel() {
        return this.Furnacelevel;
    }

    public void UpdateForItemChange() {
        if(LastHash == computeInputHash()) {
            return;
        }
        Recipe = getCurrentRecipe().orElse(null);
        this.ItemFuelTime = FuelMap.value(Inventory.getFirst().getItem());
        if (Recipe != null) {
            this.MaxBurnTime = Recipe.value().getBurnTick();
        }
        LastHash = computeInputHash();

    }
    private int computeInputHash() {
        int hash = 0;
        for (int i = 0; i < 3; i++) {
            ItemStack stack = Inventory.get(i);
            hash = 31 * hash + Objects.hash(stack.getItem(), stack.getCount());
        }
        return hash;
    }


    public Optional<RecipeEntry<ModFurnaceRecipe>> getCurrentRecipe() {
        Optional<RecipeEntry<ModFurnaceRecipe>> recipeRecipeEntry = getWorld().getServer().getRecipeManager().getFirstMatch(ModFurnaceRecipe.Type.INSTANCE , new SingleStackRecipeInput(this.Inventory.get(1)) , getWorld());
        if(!Objects.equals(recipeRecipeEntry, Optional.ofNullable(Recipe))) {
            this.BurnTime = 0;
        }
        return recipeRecipeEntry;
    }

    public boolean hasRecipe() {
        Optional<RecipeEntry<ModFurnaceRecipe>> recipe = Optional.ofNullable(Recipe);
        return recipe.isPresent() ;
    }

    public boolean canCraft() {
        Optional<RecipeEntry<ModFurnaceRecipe>> recipe = Optional.ofNullable(Recipe);
        return (this.Inventory.get(2).isEmpty() ||
                (recipe.get().value().craft(null ,null)
                        .itemMatches(this.Inventory.get(2).getRegistryEntry()) &&
                        this.Inventory.get(2).getCount() < MathHelper.clamp(this.Inventory.get(2).getMaxCount(), 1, 16)));
    }

}
