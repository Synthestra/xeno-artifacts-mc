package com.synthestra.synth_testing.xeno_artifact;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

public class XenoArtifactNodeData {
    int nodeId;

    public XenoArtifactNodeData() {}

    public XenoArtifactNodeData(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public static CompoundTag toNBT(XenoArtifactNodeData data) {
        CompoundTag dataNBT = new CompoundTag();

        dataNBT.putInt("NodeId", data.nodeId);
        return dataNBT;
    }

    public static XenoArtifactNodeData fromNBT(CompoundTag dataNBT) {
        XenoArtifactNodeData data = new XenoArtifactNodeData();

        data.setNodeId(dataNBT.getInt("NodeId"));
        return data;
    }
}
