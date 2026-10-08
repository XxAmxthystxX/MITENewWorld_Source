package com.mitenewworld.core.shadow;
import com.mitenewworld.MITENewWorld;

import net.minecraft.screen.PropertyDelegate;

public interface ShadowScreenHandler {
    void updateTick();

    PropertyDelegate getPropertyDelegate();

    boolean isOpen();

    void setOpen(boolean open);
}
