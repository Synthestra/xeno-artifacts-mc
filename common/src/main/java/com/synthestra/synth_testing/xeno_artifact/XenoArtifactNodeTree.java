package com.synthestra.synth_testing.xeno_artifact;

import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class XenoArtifactNodeTree {
    List<XenoArtifactNode> nodeTree = new ArrayList<>();
    int currentNodeId;

    public final RandomSource random = RandomSource.create();
    public final int nodeMin = 3;
    public final int nodeMax = 8;
    public final int nodeMaxEdges = 4;
    HashSet<Integer> usedNodeIds = new HashSet<>();

    public XenoArtifactNodeTree() {}

    public void setCurrentNodeId(int currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public void setNodeTree(List<XenoArtifactNode> nodeTree) {
        this.nodeTree = nodeTree;
    }

    public void addNode(XenoArtifactNode node) {
        this.nodeTree.add(node);
    }

    public int getCurrentNodeId() {
        return currentNodeId;
    }

    public List<XenoArtifactNode> getNodeTree() {
        return nodeTree;
    }

    public void generate() {
        int nodesToCreate = this.random.nextIntBetweenInclusive(this.nodeMin, this.nodeMax);

        List<XenoArtifactNode> uninitializedNodes = new ArrayList<>();
        uninitializedNodes.add(new XenoArtifactNode(this.getValidNodeId()));
        int createdNodes = 1;

        while(!uninitializedNodes.isEmpty()) {
            XenoArtifactNode node = uninitializedNodes.get(0);
            uninitializedNodes.remove(node);

            //trigger
            //effect

            int maxChildren = this.random.nextIntBetweenInclusive(1, this.nodeMaxEdges - 1);

            for (int i = 0; i < maxChildren; i++) {
                if (nodesToCreate <= createdNodes) break;

                XenoArtifactNode child = new XenoArtifactNode(this.getValidNodeId(), node.depth + 1);
                node.edges.add(child.id);
                child.edges.add(node.id);

                uninitializedNodes.add(child);
                createdNodes++;
            }
            this.nodeTree.add(node);
        }


        this.usedNodeIds.clear();

    }

    private int getValidNodeId() {
        int id = this.random.nextIntBetweenInclusive(100, 999);
        while (this.usedNodeIds.contains(id)) {
            id = this.random.nextIntBetweenInclusive(100, 999);
        }
        this.usedNodeIds.add(id);

        return id;
    }
}