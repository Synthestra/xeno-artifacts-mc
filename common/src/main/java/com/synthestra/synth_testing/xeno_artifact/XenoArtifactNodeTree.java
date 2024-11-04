package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.util.data_sets.n_node_tree.Node;

import java.util.List;

public class XenoArtifactNodeTree extends XenoArtifactNode {
    List<Integer> position;

    public final int nodeMin = 3;
    public final int nodeMax = 8;


    public XenoArtifactNodeTree(XenoArtifactNodeData data) {
        super(data);
    }

    public XenoArtifactNodeTree() {

    }

    public List<Integer> getPosition() {
        return position;
    }

    public Node<XenoArtifactNodeData> getCurrentNode() {
        Node<XenoArtifactNodeData> traversedNode = this;
        for (int j : this.position) traversedNode = traversedNode.getChildren().get(j);
        return traversedNode;
    }


    // TODO heeeeeeeeeeelp i dont know what im doing
    public XenoArtifactNodeTree generate() {
        XenoArtifactNodeTree tree = new XenoArtifactNodeTree();

        int nodesToGenerate = this.random.nextIntBetweenInclusive(this.nodeMin, this.nodeMax);

        this.generateData();

        while (nodesToGenerate >= 0) {

            nodesToGenerate = this.generateNode(nodesToGenerate);

        }


        return tree;
    }
}