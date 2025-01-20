package com.synthestra.xeno_artifacts.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactNode;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record NodeScannerPrintOutData(UUID uuid, XenoArtifactNode node) {

    public static final Codec<NodeScannerPrintOutData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.optionalFieldOf("uuid", null).forGetter(e -> e.uuid),
            XenoArtifactNode.CODEC.optionalFieldOf("node", null).forGetter(e -> e.node)
    ).apply(instance, NodeScannerPrintOutData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NodeScannerPrintOutData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, NodeScannerPrintOutData::uuid,
            XenoArtifactNode.STREAM_CODEC, NodeScannerPrintOutData::node,
            NodeScannerPrintOutData::new
    );
}
