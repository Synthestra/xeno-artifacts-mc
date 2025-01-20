package com.synthestra.xeno_artifacts.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record XenoArtifactDataGenerator(int nodesMin, int nodesMax, int nodeMaxEdges) {
    public static final Codec<XenoArtifactDataGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.optionalFieldOf("nodes_min", 3).forGetter(XenoArtifactDataGenerator::nodesMin),
            Codec.INT.optionalFieldOf("nodes_max", 8).forGetter(XenoArtifactDataGenerator::nodesMax),
            Codec.INT.optionalFieldOf("node_max_edges", 3).forGetter(XenoArtifactDataGenerator::nodeMaxEdges))
            .apply(instance, XenoArtifactDataGenerator::new));

}
