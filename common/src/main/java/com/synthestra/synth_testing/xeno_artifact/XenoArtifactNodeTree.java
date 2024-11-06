package com.synthestra.synth_testing.xeno_artifact;

import net.minecraft.util.RandomSource;

import java.util.*;

public class XenoArtifactNodeTree {
    List<XenoArtifactNode> nodeTree = new ArrayList<>();

    public final RandomSource random = RandomSource.create();
    public final int nodeMin = 3;
    public final int nodeMax = 8;
    public final int nodeMaxEdges = 4;
    HashSet<Integer> usedNodeIds = new HashSet<>();

    public XenoArtifactNodeTree() {}

    public void setNodeTree(List<XenoArtifactNode> nodeTree) {
        this.nodeTree = nodeTree;
    }

    public void addNode(XenoArtifactNode node) {
        this.nodeTree.add(node);
    }

    public List<XenoArtifactNode> getNodeTree() {
        return nodeTree;
    }

    public XenoArtifactNode getRootNode() {
        return this.nodeTree.stream().filter(e -> e.getDepth() == 0).findFirst().orElseThrow();
    }

    public XenoArtifactNode getNodeFromId(int id) {
        return this.nodeTree.stream().filter(e -> e.getId() == id).findFirst().orElseThrow();
    }

    public List<XenoArtifactNode> getEdgesOf(XenoArtifactNode node) {
        List<XenoArtifactNode> edges = new ArrayList<>();
        node.edges.forEach(e -> edges.add(this.getNodeFromId(e)));
        return edges;
    }

    public void generate() {
        int nodesToCreate = this.random.nextIntBetweenInclusive(this.nodeMin, this.nodeMax);

        Queue<XenoArtifactNode> uninitializedNodes = new LinkedList<>();
        uninitializedNodes.add(new XenoArtifactNode(this.getValidNodeId()));
        int createdNodes = 1;

        while(!uninitializedNodes.isEmpty()) {
            XenoArtifactNode node = uninitializedNodes.remove();

            node.trigger = XenoArtifactTrigger.pick(random);
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