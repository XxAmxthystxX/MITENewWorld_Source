package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.MapCodec;

/** 合金炉零件：InputPort。 */
public class ModBlastFurnaceInputPortBlock extends AbstractBlastFurnacePortBlock {

    public static final MapCodec<ModBlastFurnaceInputPortBlock> CODEC = createCodec(ModBlastFurnaceInputPortBlock::new);

    /** 数据驱动用，默认 1 级 */
    public ModBlastFurnaceInputPortBlock(Settings settings) {
        this(1, settings);
    }

    public ModBlastFurnaceInputPortBlock(int tier, Settings settings) {
        super(PartKind.INPUT_PORT, tier, settings);
    }

    @Override
    protected MapCodec<ModBlastFurnaceInputPortBlock> getCodec() {
        return CODEC;
    }
}
