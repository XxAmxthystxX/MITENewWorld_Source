package com.mitenewworld.mixin.servermixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.recipe.ModCraftingRecipe;
import com.mitenewworld.core.shadow.ShadowScreenHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractCraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractCraftingScreenHandler implements ShadowScreenHandler {
    @Unique
    public boolean isOpen = false;
    @Unique
    public int progress = 0;
    @Unique
    public int maxprogress = 100;
    @Unique
    public int craftProperty = 0;
    @Unique
    public int craftState = 0;
    /**
     * 合成轮次：每开始一轮合成 +1。客户端靠它判断"新的一轮开始了"，
     * 之后进度由客户端本地按 tick 推算，服务端不再逐 tick 下发进度。
     */
    @Unique
    public int craftRound = 0;
    @Mutable
    @Final
    @Shadow
    public final boolean onServer;
    @Mutable
    @Final
    @Shadow
    private final PlayerEntity owner;
    @Unique
    protected int lastInputHash = 0;
    @Unique
    protected @Nullable RecipeEntry<ModCraftingRecipe> Recipe;
    @Unique
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> 0;             // 逐 tick 进度已废弃：不再下发，客户端本地推算
                case 1 -> maxprogress;
                case 2 -> craftProperty;
                case 3 -> craftState;
                case 4 -> craftRound;
                default -> 0;
            };
        }
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 1 -> maxprogress = value;
                case 2 -> craftProperty = value;
                case 3 -> craftState = value;
                case 4 -> craftRound = value;
            }

        }
        @Override
        public int size() {
            return 5;
        }
    };
    @Override
    protected Slot addResultSlot(PlayerEntity player, int x, int y) {
        return this.addSlot(new Slot(this.craftingResultInventory, 0, x, y));
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.addProperties(propertyDelegate);
    }

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, boolean onServer, PlayerEntity owner, RecipeInputInventory craftingInput, CraftingResultInventory craftingResult, boolean onServer1, PlayerEntity owner1, CraftingResultInventory craftingResult1, boolean onServer2, PlayerEntity owner2) {
        super(type, syncId, 0, 0);
        this.onServer = onServer2;
        this.owner = owner2;
    }
    @Override
    @Unique
    public void updateTick() {
        if (owner.getEntityWorld().isClient()) {
            return;
        }
        int prevProperty = craftProperty;
        int prevState = craftState;
        int prevRound = craftRound;

        Optional<RecipeEntry<ModCraftingRecipe>> currentRecipe = getCurrentRecipe();
        boolean hasValidRecipe = currentRecipe.isPresent();
        if (hasValidRecipe) {
            // 进度上限取配方自己的 craftingtick（与模组工作台一致），不再写死 100
            int tick = Math.max(1, currentRecipe.get().value().getCraftTick());
            if (tick != maxprogress) {
                maxprogress = tick;
                resetProgress();
            }
            if (canInsertResult() && matchLevel()) {
                craftProperty = 1;
            } else if (canInsertResult()) {
                craftProperty = 2;
            } else {
                craftProperty = 3;
            }
        } else {
            craftProperty = 0;
            craftState = 0;
            maxprogress = 100;
            resetProgress();
        }
        if (craftState == 1 && craftProperty == 1) {
            progress++;
            if (progress >= maxprogress) {
                craftItem();
                resetProgress();
                removeStack(this.craftingInventory.getHeldStacks());
                // 自动连做时通知客户端"新一轮开始"（一轮一次，不是每 tick）
                craftRound++;
            }
        } else {
            resetProgress();
        }
        // 只在真正发生变化时同步一次；原版 ServerPlayerEntity.tick 本来就会每 tick
        // sendContentUpdates()，这里再每 tick updateToClient() 是纯重复
        if (craftProperty != prevProperty || craftState != prevState || craftRound != prevRound) {
            updateToClient();
        }
    }

    @Override
    @Unique
    public PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Unique
    private boolean canInsertResult() {
        Optional<RecipeEntry<ModCraftingRecipe>> recipe = getCurrentRecipe();
        ItemStack resultStack = recipe.get().value().craft( null,null);
        ItemStack currentResult = craftingResultInventory.getStack(0);

        if (currentResult.isEmpty()) {return true;}

        return ItemStack.areEqual(resultStack,currentResult) &&
                currentResult.getCount() + resultStack.getCount() <= currentResult.getMaxCount();
    }
    @Override
    public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
        RecipeEntry<ModCraftingRecipe> recipeEntry = (RecipeEntry<ModCraftingRecipe>) recipe;
        this.onInputSlotFillStart();
        try {
            List<Slot> list = this.getInputSlots();
            return InputSlotFiller.fill(new InputSlotFiller.Handler<>() {
                @Override
                public void populateRecipeFinder(RecipeFinder finder) {
                    PlayerScreenHandlerMixin.this.FpopulateRecipeFinder(finder);
                }

                @Override
                public void clear() {
                    PlayerScreenHandlerMixin.this.craftingResultInventory.clear();
                    PlayerScreenHandlerMixin.this.craftingInventory.clear();
                }

                @Override
                public boolean matches(RecipeEntry<ModCraftingRecipe> entry) {
                    return entry.value().matches(PlayerScreenHandlerMixin.this.craftingInventory.createRecipeInput(), PlayerScreenHandlerMixin.this.getPlayer().getEntityWorld());
                }
            }, 2, 2, list, list, inventory, recipeEntry, craftAll, creative);
        } catch (Exception e) {
            return PostFillAction.NOTHING;
        }

    }

    private void FpopulateRecipeFinder(RecipeFinder finder) {
        this.craftingInventory.provideRecipeInputs(finder);
    }

    @Override
    @Unique
    public boolean onButtonClick(PlayerEntity player, int id) {
        if ((craftProperty == 1) && id == 0) {
            this.craftState = 1;
            this.craftRound++;     // 通知客户端：新一轮合成开始
            return true;
        }
        return false;
    }

    @Shadow
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Shadow
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Inject(method = "onContentChanged", at = @At("HEAD"), cancellable = true)
    public void onContentChanged(Inventory inventory, CallbackInfo ci) {
        ci.cancel();
    }


    @Unique
    public void removeStack(List<ItemStack> inputStacks) {
        for (ItemStack itemStack : inputStacks) {
            itemStack.decrement(1);
            if (!itemStack.getRecipeRemainder().isEmpty()) {
                ItemStack remainder = itemStack.getRecipeRemainder().copy();
                World world = owner.getEntityWorld();
                owner.getEntityWorld().spawnEntity(new ItemEntity(world, owner.getBlockX() , owner.getBlockY() , owner.getBlockZ(), remainder));
            }
        }
    }

    @Unique
    public void craftItem() {
        Optional<RecipeEntry<ModCraftingRecipe>> currentRecipe = getCurrentRecipe();
        currentRecipe.ifPresent(modCraftingTableRecipeRecipeEntry
                -> this.craftingResultInventory.setStack(0, new ItemStack(modCraftingTableRecipeRecipeEntry.value().craft(null , null).getItem(),
                this.craftingResultInventory.getStack(0).getCount() + modCraftingTableRecipeRecipeEntry.value().craft(null ,null).getCount())));
    }
    @Inject(method = "onClosed" , at = @At(value = "HEAD"))
    public void FixedOnClosed(PlayerEntity player, CallbackInfo ci) {
        dropInventory(player, this.craftingResultInventory);
        dropInventory(player, this.craftingInventory);
        this.isOpen = false;
    }

    @Unique
    public Optional<RecipeEntry<ModCraftingRecipe>> getCurrentRecipe() {
        int currentHash = computeInputHash();
        if (currentHash == lastInputHash && Recipe != null) {
            return Optional.of(Recipe);
        }
        CraftingRecipeInput input = this.craftingInventory.createPositionedRecipeInput().input();
        Optional<RecipeEntry<ModCraftingRecipe>> newRecipe =
                this.owner.getEntityWorld().getServer().getRecipeManager().getFirstMatch(ModCraftingRecipe.Type.INSTANCE, input, this.owner.getEntityWorld());
        lastInputHash = currentHash;
        Recipe = newRecipe.orElse(null);
        return newRecipe;
    }
    @Unique
    public boolean matchLevel(){
        Optional<RecipeEntry<ModCraftingRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent() && recipe.get().value().getCraftLevel() == 0;
    }
    @Unique
    private int computeInputHash() {
        int hash = 0;
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.craftingInventory.getStack(i);
            hash = 31 * hash + Objects.hash(stack.getItem(), stack.getCount());
        }
        return hash;
    }
    @Unique
    public void resetProgress() {
        this.progress = 0;
    }
    public List<Slot> getInputSlots(){
        return this.slots.subList(1, 5);
    }
    public Slot getOutputSlot(){
        return this.slots.getFirst();
    }

}
