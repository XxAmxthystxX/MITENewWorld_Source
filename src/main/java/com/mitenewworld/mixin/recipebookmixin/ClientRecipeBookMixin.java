package com.mitenewworld.mixin.recipebookmixin;
import com.mitenewworld.MITENewWorld;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import com.mitenewworld.core.shadow.ShadowClientRecipeBook;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;


@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin implements ShadowClientRecipeBook {
    @Shadow
    private final Map<NetworkRecipeId, RecipeDisplayEntry> recipes = new HashMap<>();
    ;
    @Unique
    private Map<RecipeBookGroup, List<ModRecipeResultCollection>> fixedResultsByCategory = Map.of();

    @Unique
    private List<ModRecipeResultCollection> fixedOrderedResults = List.of();


    @Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
    private void refresh(CallbackInfo ci) {
        ci.cancel();
        Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> map = toGroupedMap(this.recipes.values());
        Map<RecipeBookGroup, List<ModRecipeResultCollection>> map2 = new HashMap<>();
        ImmutableList.Builder<ModRecipeResultCollection> builder = ImmutableList.builder();
        map.forEach(
                (group, resultCollections) -> map2.put(
                        group, resultCollections.stream().map(ModRecipeResultCollection::new).peek(builder::add).collect(ImmutableList.toImmutableList())
                )
        );

        for (RecipeBookType recipeBookType : RecipeBookType.values()) {
            map2.put(
                    recipeBookType,
                    recipeBookType.getCategories()
                            .stream()
                            .flatMap(group -> map2.getOrDefault(group, List.of()).stream())
                            .collect(ImmutableList.toImmutableList())
            );
        }

        this.fixedResultsByCategory = Map.copyOf(map2);
        this.fixedOrderedResults = builder.build();

    }
    @Shadow
    private static Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> toGroupedMap(Iterable<RecipeDisplayEntry> recipes) {
        Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> map = new HashMap<>();
        Table<RecipeBookCategory, Integer, List<RecipeDisplayEntry>> table = HashBasedTable.create();

        for (RecipeDisplayEntry recipeDisplayEntry : recipes) {
            RecipeBookCategory recipeBookCategory = recipeDisplayEntry.category();
            OptionalInt optionalInt = recipeDisplayEntry.group();
            if (optionalInt.isEmpty()) {
                map.computeIfAbsent(recipeBookCategory, group -> new ArrayList<>()).add(List.of(recipeDisplayEntry));
            } else {
                List<RecipeDisplayEntry> list = table.get(recipeBookCategory, optionalInt.getAsInt());
                if (list == null) {
                    list = new ArrayList<>();
                    table.put(recipeBookCategory, optionalInt.getAsInt(), list);
                    map.computeIfAbsent(recipeBookCategory, group -> new ArrayList<>()).add(list);
                }

                list.add(recipeDisplayEntry);
            }
        }

        return map;
    }

    @Override
    public List<ModRecipeResultCollection> getFixedResultsByCategory(RecipeBookGroup category) {
        return fixedResultsByCategory.getOrDefault(category, Collections.emptyList());
    }
    @Override
    public List<ModRecipeResultCollection> getFixedOrderedResults() {
        return fixedOrderedResults;
    }
}
