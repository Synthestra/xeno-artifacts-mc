package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.xeno_artifact.events.Reaction;
import com.synthestra.synth_testing.xeno_artifact.triggers.Trigger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class XenoArtifactNode {
    int id;
    int depth;
    HashSet<Integer> edges = new HashSet<>();
    XenoArtifactTrigger trigger;
    XenoArtifactReaction reaction;

    public XenoArtifactNode() {}

    public XenoArtifactNode(int id) {
        this.id = id;
    }

    public XenoArtifactNode(int id, int depth) {
        this.id = id;
        this.depth = depth;
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

    public void addEdges(int edge) {
        edges.add(edge);
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

    public Trigger getTriggerValue() {
        return trigger.getTrigger();
    }

    public void setReaction(XenoArtifactReaction reaction) {
        this.reaction = reaction;
    }

    public XenoArtifactReaction getReaction() {
        return reaction;
    }

    public Reaction getReactionValue() {
        return reaction.getReaction();
    }
}