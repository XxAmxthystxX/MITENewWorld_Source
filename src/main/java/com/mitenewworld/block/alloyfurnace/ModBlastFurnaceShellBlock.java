package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.MapCodec;

/** 合金炉零件：Shell。 */
public class ModBlastFurnaceShellBlock extends AbstractBlastFurnacePartBlock {

    public static final MapCodec<ModBlastFurnaceShellBlock> CODEC = createCodec(ModBlastFurnaceShellBlock::new);

    /** 数据驱动用，默认 1 级 */
    public ModBlastFurnaceShellBlock(Settings settings) {
        this(1, settings);
    }

    public ModBlastFurnaceShellBlock(int tier, Settings settings) {
        super(PartKind.SHELL, tier, settings);
    }

    @Override
    protected MapCodec<ModBlastFurnaceShellBlock> getCodec() {
        return CODEC;
    }
}
