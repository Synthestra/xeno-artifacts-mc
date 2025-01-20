package com.synthestra.xeno_artifacts.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactNode;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.UUID;

public record XenoArtifactData(UUID uuid, int currentId, List<XenoArtifactNode> tree, boolean directionBias) {

    public static final Codec<XenoArtifactData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(e -> e.uuid),
            Codec.INT.fieldOf("current_id").forGetter(e -> e.currentId),
            XenoArtifactNode.CODEC.listOf().fieldOf("tree").forGetter(e -> e.tree),
            Codec.BOOL.fieldOf("direction_bias").forGetter(e -> e.directionBias)
    ).apply(instance, XenoArtifactData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, XenoArtifactData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, XenoArtifactData::uuid,
            ByteBufCodecs.INT, XenoArtifactData::currentId,
            XenoArtifactNode.STREAM_CODEC.apply(ByteBufCodecs.list()), XenoArtifactData::tree,
            ByteBufCodecs.BOOL, XenoArtifactData::directionBias,
            XenoArtifactData::new
    );
}
