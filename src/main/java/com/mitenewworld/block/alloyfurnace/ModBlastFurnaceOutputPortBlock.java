package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.MapCodec;

/** 合金炉零件：OutputPort。 */
public class ModBlastFurnaceOutputPortBlock extends AbstractBlastFurnacePortBlock {

    public static final MapCodec<ModBlastFurnaceOutputPortBlock> CODEC = createCodec(ModBlastFurnaceOutputPortBlock::new);

    /** 数据驱动用，默认 1 级 */
    public ModBlastFurnaceOutputPortBlock(Settings settings) {
        this(1, settings);
    }

    public ModBlastFurnaceOutputPortBlock(int tier, Settings settings) {
        super(PartKind.OUTPUT_PORT, tier, settings);
    }

    @Override
    protected MapCodec<ModBlastFurnaceOutputPortBlock> getCodec() {
        return CODEC;
    }
}
