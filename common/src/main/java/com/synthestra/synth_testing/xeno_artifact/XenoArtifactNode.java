package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.util.data_sets.n_node_tree.Node;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;

public class XenoArtifactNode extends Node<XenoArtifactNodeData> {
    XenoArtifactNodeData data;
    ArrayList<Node<XenoArtifactNodeData>> children;

    public final int nodeMaxEdges = 4;
    public final RandomSource random = RandomSource.create();

    public XenoArtifactNode(XenoArtifactNodeData data) {
        super(data);
        this.data = data;
        this.children = new ArrayList<>();
    }

    public XenoArtifactNode() {

    }

    public void generateData() {
        RandomSource random = RandomSource.create();
        this.data.nodeId = random.nextIntBetweenInclusive(100, 999);
    }

    // TODO heeeeeeeeeeelp i dont know what im doing
    public int generateNode(int nodesToGenerate) {
        int maxChildren = this.random.nextIntBetweenInclusive(1, this.nodeMaxEdges - 1);
        this.generateData();
        for (int i = 0; i < maxChildren; i++) {
            XenoArtifactNode node = new XenoArtifactNode();
            this.addChildren(node);
        }


        return nodesToGenerate - maxChildren;
    }

//    public static CompoundTag toNBT(XenoArtifactNodeData nodes) {
//        ListTag nodesNBT = new ListTag();
//        for (XenoArtifactNodeData node : nodes) {
//            CompoundTag nodeNBT = new CompoundTag();
//
//            nodeNBT.putInt("NodeId", node.nodeId);
//            nodeNBT.put("Children", node.children);
//            nodesNBT.add(nodeNBT);
//        }
//        return nodesNBT;
//    }
//
//    public static XenoArtifactNode fromNBT(ListTag nodesNBT) {
//        ArrayList<XenoArtifactNodeData> nodes = new ArrayList<>();
//        for (CompoundTag nodeNBT : nodesNBT.stream().map(CompoundTag.class::cast).toList()) {
//            nodes.add(new XenoArtifactNodeData(
//                    nodeNBT.get("Children"),
//                    nodeNBT.getInt("NodeId"),
//
//                    XenoArtifactTriggers.valueOf(nodeNBT.getString("Trigger")).getTrigger()
//            ));
//        }
//        return nodes;
//    }
}