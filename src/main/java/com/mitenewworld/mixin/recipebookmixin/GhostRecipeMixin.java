package com.mitenewworld.mixin.recipebookmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.core.shadow.ShadowGhostRecipe;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GhostRecipe.class)
public class GhostRecipeMixin implements ShadowGhostRecipe {

    @Override
    public void pubAddInputs(Slot slot, ContextParameterMap context, SlotDisplay display) {
        addInputs(slot, context, display);
    }

    @Override
    public void pubAddResult(Slot slot, ContextParameterMap context, SlotDisplay display) {
        addResults(slot, context, display);
    }

    @Shadow
    protected void addInputs(Slot slot, ContextParameterMap context, SlotDisplay display)
    {
    }
    @Shadow
    protected void addResults(Slot slot, ContextParameterMap context, SlotDisplay display)
    {}
}
