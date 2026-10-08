package com.mitenewworld.core.shadow;
import com.mitenewworld.MITENewWorld;

import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.context.ContextParameterMap;

public interface ShadowGhostRecipe {
    void pubAddInputs(Slot slot, ContextParameterMap context, SlotDisplay display);

    void pubAddResult(Slot slot, ContextParameterMap context, SlotDisplay display);
}
