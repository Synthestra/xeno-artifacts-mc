package com.synthestra.synth_testing.block.entity;

import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNode;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNodeTree;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class XenoArtifactBlockEntity extends BlockEntity {
    protected XenoArtifactNodeTree nodeTree = new XenoArtifactNodeTree();
    protected int currentNodeId;
    protected Direction directionBias = Direction.DOWN;

    public final RandomSource random = RandomSource.create();

    public XenoArtifactBlockEntity(BlockPos pos, BlockState blockState) {
        super(SynBlockEntityTypes.XENO_ARTIFACT.get(), pos, blockState);
    }

    public void generateTree() {
        this.nodeTree.generate();
        this.currentNodeId = this.nodeTree.getRootNode().getId();
    }

    public void setCurrentNodeId(int currentNodeId) {
        this.currentNodeId = currentNodeId;

    }

    public Direction getDirectionBias() {
        return directionBias;
    }

    public void setDirectionBias(Direction direction) {
        this.directionBias = direction;
        this.setChanged();
    }



    public void getNewNode() {
        XenoArtifactNode currentNode = nodeTree.getNodeFromId(this.currentNodeId);

        List<XenoArtifactNode> edges = nodeTree.getEdgesOf(currentNode);
        switch (this.directionBias) {
            case UP -> {
                List<XenoArtifactNode> upNodes = edges.stream().filter(e -> e.getDepth() < currentNode.getDepth()).toList();
                if (!upNodes.isEmpty()) edges = upNodes;
            }
            case DOWN -> {
                List<XenoArtifactNode> downNodes = edges.stream().filter(e -> e.getDepth() > currentNode.getDepth()).toList();
                if (!downNodes.isEmpty()) edges = downNodes;
            }

        }
        XenoArtifactNode newNode = edges.get(random.nextInt(edges.size()));
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(newNode.getTrigger().getTrigger().customMessage()));
        currentNodeId = newNode.getId();

        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.currentNodeId = tag.getInt("current_node_id");
        this.directionBias = Direction.byName(tag.getString("direction_bias"));

        ListTag nodeTreeNBT = tag.getList("node_tree", Tag.TAG_COMPOUND);
        this.nodeTree = new XenoArtifactNodeTree();
        for (int i = 0; i < nodeTreeNBT.size(); i++) {
            CompoundTag nodeNBT = nodeTreeNBT.getCompound(i);
            XenoArtifactNode node = new XenoArtifactNode();

            node.setId(nodeNBT.getInt("id"));
            node.setDepth(nodeNBT.getInt("depth"));
            node.setEdges(node.getEdgesFromIntArray(nodeNBT.getIntArray("edges")));
            node.setTrigger(XenoArtifactTrigger.byName(nodeNBT.getString("trigger")));

            this.nodeTree.addNode(node);
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("current_node_id", this.currentNodeId);
        tag.putString("direction_bias", this.directionBias.getName());

        ListTag nodeTreeNBT = new ListTag();
        for (XenoArtifactNode node : this.nodeTree.getNodeTree()) {
            CompoundTag nodeNBT = new CompoundTag();

            nodeNBT.putInt("id", node.getId());
            nodeNBT.putInt("depth", node.getDepth());
            nodeNBT.putIntArray("edges", node.getEdgesIntArray());
            nodeNBT.putString("trigger", node.getTrigger().getSerializedName());

            nodeTreeNBT.add(nodeNBT);
        }
        tag.put("node_tree", nodeTreeNBT);
    }
}
