package com.synthestra.synth_testing.xeno_artifact;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class XenoArtifactNode {
    int id;
    int depth;
    HashSet<Integer> edges = new HashSet<>();
    XenoArtifactTrigger trigger;
    XenoArtifactEffect effect;

    public XenoArtifactNode() {}

    public XenoArtifactNode(int id) {
        this.id = id;
    }

    public XenoArtifactNode(int id, int depth) {
        this.id = id;
        this.depth = depth;
    }

    public XenoArtifactNode(int id, int depth, HashSet<Integer> edges) {
        this.id = id;
        this.depth = depth;
        this.edges = edges;
    }

    public XenoArtifactNode(int id, int depth, HashSet<Integer> edges, XenoArtifactTrigger trigger) {
        this.id = id;
        this.depth = depth;
        this.edges = edges;
        this.trigger = trigger;
    }

    public XenoArtifactNode(int id, int depth, HashSet<Integer> edges, XenoArtifactTrigger trigger, XenoArtifactEffect effect) {
        this.id = id;
        this.depth = depth;
        this.edges = edges;
        this.trigger = trigger;
        this.effect = effect;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setEdges(HashSet<Integer> edges) {
        this.edges = edges;
    }

    public HashSet<Integer> getEdges() {
        return edges;
    }

    public int[] getEdgesIntArray() {
        return edges.stream().mapToInt(Number::intValue).toArray();
    }

    public HashSet<Integer> getEdgesFromIntArray(int[] edgesIntArray) {
        return Arrays.stream(edgesIntArray).boxed().collect(Collectors.toCollection(HashSet::new));
    }

    public void setTrigger(XenoArtifactTrigger trigger) {
        this.trigger = trigger;
    }

    public XenoArtifactTrigger getTrigger() {
        return trigger;
    }
}