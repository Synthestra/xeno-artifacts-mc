package com.synthestra.synth_testing.block.entity;

import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNodeTree;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class XenoArtifactBlockEntity extends BlockEntity {
    protected XenoArtifactNodeTree nodeTree;
    protected List<Integer> currentNodeId = new ArrayList<>();

    public XenoArtifactBlockEntity(BlockPos pos, BlockState blockState) {
        super(SynBlockEntityTypes.XENO_ARTIFACT.get(), pos, blockState);
    }

    public void generateTree() {
        this.nodeTree = new XenoArtifactNodeTree();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        //this.nodeTree = tag.getList("NodeTree", Tag.TAG_COMPOUND);
        this.currentNodeId = Arrays.stream(tag.getIntArray("CurrentNodeId")).boxed().collect(Collectors.toList());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putIntArray("CurrentNodeId", this.currentNodeId);
        //tag.put("NodeTree", XenoArtifactNodeTree.toNBT(this.nodeTree));
    }
}
