package com.synthestra.synth_testing.block.entity;

import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNode;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNodeTree;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class XenoArtifactBlockEntity extends BlockEntity {
    protected XenoArtifactNodeTree nodeTree;
    protected int currentNodeId;

    public XenoArtifactBlockEntity(BlockPos pos, BlockState blockState) {
        super(SynBlockEntityTypes.XENO_ARTIFACT.get(), pos, blockState);
    }

    public void generateTree() {
        this.nodeTree = new XenoArtifactNodeTree();
        this.nodeTree.generate();
        this.setCurrentNodeId(this.nodeTree.getRootNode().getId());
    }

    public void setCurrentNodeId(int currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.currentNodeId = tag.getInt("current_node_id");

        ListTag nodeTreeNBT = tag.getList("node_tree", Tag.TAG_COMPOUND);
        //this.nodeTree = new XenoArtifactNodeTree();
        for (int i = 0; i < nodeTreeNBT.size(); i++) {
            CompoundTag nodeNBT = nodeTreeNBT.getCompound(i);
            XenoArtifactNode node = new XenoArtifactNode();

            node.setId(nodeNBT.getInt("id"));
            node.setDepth(nodeNBT.getInt("depth"));
            node.setEdges(node.getEdgesFromIntArray(nodeNBT.getIntArray("edges")));
            this.nodeTree.addNode(node);
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("current_node_id", this.currentNodeId);

        ListTag nodeTreeNBT = new ListTag();
        for (XenoArtifactNode node : this.nodeTree.getNodeTree()) {
            CompoundTag nodeNBT = new CompoundTag();

            nodeNBT.putInt("id", node.getId());
            nodeNBT.putInt("depth", node.getDepth());
            nodeNBT.putIntArray("edges", node.getEdgesIntArray());

            nodeTreeNBT.add(nodeNBT);
        }
        tag.put("node_tree", nodeTreeNBT);
    }

    // { current_node_id: 434, node_tree:[ {id: 434, depth: 0, } ] }
}
