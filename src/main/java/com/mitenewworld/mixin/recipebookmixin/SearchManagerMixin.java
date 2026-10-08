package com.mitenewworld.mixin.recipebookmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import com.mitenewworld.core.shadow.ShadowClientRecipeBook;
import com.mitenewworld.core.shadow.ShadowSearchManager;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.search.SearchProvider;
import net.minecraft.client.search.TextSearchProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Mixin(SearchManager.class)
public class SearchManagerMixin implements ShadowSearchManager {
    @Unique
    private CompletableFuture<SearchProvider<ModRecipeResultCollection>> FixedRecipeOutputReloadFuture = CompletableFuture.completedFuture(SearchProvider.empty());

    @Shadow
    private static final SearchManager.Key RECIPE_OUTPUT = null;

    @Inject(method = "addRecipeOutputReloader" ,at = @At(value = "HEAD"), cancellable = true)
    private void addRecipeOutputReloaderFixed(ClientRecipeBook recipeBook, World world, CallbackInfo ci) {
        ci.cancel();
        this.addReloader(RECIPE_OUTPUT, () -> {
            List<ModRecipeResultCollection> list = ((ShadowClientRecipeBook)recipeBook).getFixedOrderedResults();
            DynamicRegistryManager dynamicRegistryManager = world.getRegistryManager();
            Registry<Item> registry = dynamicRegistryManager.getOrThrow(RegistryKeys.ITEM);
            Item.TooltipContext tooltipContext = Item.TooltipContext.create(dynamicRegistryManager);
            ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(world);
            TooltipType.Default tooltipType = TooltipType.Default.BASIC;
            CompletableFuture<SearchProvider<ModRecipeResultCollection>> completableFuture = this.FixedRecipeOutputReloadFuture;
            this.FixedRecipeOutputReloadFuture = CompletableFuture.supplyAsync(() ->
                    new TextSearchProvider<>(resultCollection ->
                            collectItemTooltips(resultCollection.getAllRecipes()
                                    .stream()
                                    .flatMap(display ->
                                            display.getStacks(contextParameterMap).stream()), tooltipContext, tooltipType), resultCollection ->
                            resultCollection.getAllRecipes().stream().flatMap(display ->
                                    display.getStacks(contextParameterMap).stream()).map(stack ->
                                    registry.getId(stack.getItem())), list), Util.getMainWorkerExecutor());
            completableFuture.cancel(true);
        });
    }
    @Shadow
    private void addReloader(SearchManager.Key key, Runnable reloader) {
    }
    @Shadow
    private static Stream<String> collectItemTooltips(Stream<ItemStack> stacks, Item.TooltipContext context, TooltipType type) {
        return stacks.flatMap(stack -> stack.getTooltip(context, null, type).stream()).map(tooltip -> Formatting.strip(tooltip.getString()).trim()).filter(string -> !string.isEmpty());
    }

    @Override
    public SearchProvider<ModRecipeResultCollection> getFixedRecipeOutputReloadFuture() {
        return FixedRecipeOutputReloadFuture.join();
    }
}
