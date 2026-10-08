package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import net.minecraft.block.Block;

/**
 * 合金炉多方块零件：外壳 / 原料口 / 产物口。
 * 只作为结构标记，无方块实体。
 */
public abstract class AbstractBlastFurnacePartBlock extends Block {

    public enum PartKind { SHELL, INPUT_PORT, OUTPUT_PORT }

    private final PartKind kind;
    private final int tier;

    protected AbstractBlastFurnacePartBlock(PartKind kind, int tier, Settings settings) {
        super(settings);
        this.kind = kind;
        this.tier = tier;
    }

    public PartKind kind() { return kind; }

    /** 1~5 */
    public int tier() { return tier; }
}
