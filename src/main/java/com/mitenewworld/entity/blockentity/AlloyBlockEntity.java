package com.mitenewworld.entity.blockentity;

import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

public class AlloyBlockEntity extends BlockEntity {

    private AlloyComponent alloy;

    public AlloyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOY_BLOCK_ENTITY, pos, state);
    }

    public AlloyComponent alloy() {
        return alloy;
    }

    public void setAlloy(AlloyComponent alloy) {
        this.alloy = alloy;
        markDirty();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (alloy != null) {
            view.put("alloy", AlloyComponent.CODEC, alloy);
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        alloy = view.read("alloy", AlloyComponent.CODEC).orElse(null);
    }
}
