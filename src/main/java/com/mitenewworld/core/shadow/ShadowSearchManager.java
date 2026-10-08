package com.mitenewworld.core.shadow;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import net.minecraft.client.search.SearchProvider;

public interface ShadowSearchManager {

    SearchProvider<ModRecipeResultCollection> getFixedRecipeOutputReloadFuture() ;
}
