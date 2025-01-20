package com.synthestra.xeno_artifacts.xeno_artifact;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.synthestra.xeno_artifacts.xeno_artifact.events.Reaction;
import com.synthestra.xeno_artifacts.xeno_artifact.triggers.Trigger;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class XenoArtifactNode {
    int id;
    int depth;
    IntList edges = new IntArrayList();
    XenoArtifactTrigger trigger;
    XenoArtifactReaction reaction;
    boolean activated;

    public XenoArtifactNode() {}

    public XenoArtifactNode(int id) {
        this.id = id;
    }

    public XenoArtifactNode(int id, int depth) {
        this.id = id;
        this.depth = depth;
    }

    public XenoArtifactNode(Integer id, Integer depth, IntList edges, String trigger, String reaction, Boolean activated) {
        this.id = id;
        this.depth = depth;
        this.edges = edges;
        this.trigger = XenoArtifactTrigger.byName(trigger);
        this.reaction = XenoArtifactReaction.byName(reaction);
        this.activated = activated;
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

    public void setEdges(IntList edges) {
        this.edges = edges;
    }

    public IntList getEdges() {
        return edges;
    }

    public void addEdges(int edge) {
        edges.add(edge);
    }

    public int[] getEdgesIntArray() {
        return edges.stream().mapToInt(Number::intValue).toArray();
    }

    public int[] getEdgesIntArray(IntList a) {
        return a.stream().mapToInt(Number::intValue).toArray();
    }

    public HashSet<Integer> getEdgesFromIntArray(int[] edgesIntArray) {
        return Arrays.stream(edgesIntArray).boxed().collect(Collectors.toCollection(HashSet::new));
    }

    public HashSet<Integer> getEdgesFromIntArray(IntList edgesIntList) {
        return Arrays.stream(edgesIntList.toIntArray()).boxed().collect(Collectors.toCollection(HashSet::new));
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

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return this.activated;
    }

    @Override
    public String toString() {
        return "XenoArtifactNode{" +
            "id=" + id +
            ", depth=" + depth +
            ", edges=" + edges +
            ", trigger=" + trigger +
            ", reaction=" + reaction +
            ", activated=" + activated +
            '}';
    }

    public static final Codec<IntList> EDGES_CODEC = Codec.INT.listOf().xmap(IntArrayList::new, ArrayList::new);
    public static final StreamCodec<ByteBuf, IntList> EDGES_STREAM_CODEC = ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(IntArrayList::new, ArrayList::new);

    public static final Codec<XenoArtifactNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(e -> e.id),
            Codec.INT.fieldOf("depth").forGetter(e -> e.depth),
            EDGES_CODEC.fieldOf("edges").forGetter(e -> e.edges),
            Codec.STRING.fieldOf("trigger").forGetter(e -> String.valueOf(e.trigger)),
            Codec.STRING.fieldOf("reaction").forGetter(e -> String.valueOf(e.reaction)),
            Codec.BOOL.fieldOf("activated").forGetter(e -> e.activated)
    ).apply(instance, XenoArtifactNode::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, XenoArtifactNode> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, XenoArtifactNode::getId,
            ByteBufCodecs.INT, XenoArtifactNode::getDepth,
            EDGES_STREAM_CODEC, XenoArtifactNode::getEdges,
            ByteBufCodecs.STRING_UTF8, XenoArtifactNode::triggerString, //todo change to enumcodec
            ByteBufCodecs.STRING_UTF8, XenoArtifactNode::reactionString,
            ByteBufCodecs.BOOL, XenoArtifactNode::isActivated,
            XenoArtifactNode::new
    );

    public String triggerString() {
        return this.trigger.toString();
    }

    public String reactionString() {
        return this.reaction.toString();
    }
}