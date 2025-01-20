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

public record XenoArtifactScrapbookPage(UUID uuid, List<XenoArtifactNode> notes) {

    public static final Codec<XenoArtifactScrapbookPage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(e -> e.uuid),
            XenoArtifactNode.CODEC.listOf().fieldOf("notes").forGetter(e -> e.notes)
    ).apply(instance, XenoArtifactScrapbookPage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, XenoArtifactScrapbookPage> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, XenoArtifactScrapbookPage::uuid,
            XenoArtifactNode.STREAM_CODEC.apply(ByteBufCodecs.list()), XenoArtifactScrapbookPage::notes,
            XenoArtifactScrapbookPage::new
    );

    public XenoArtifactScrapbookPage copy() {
        return new XenoArtifactScrapbookPage(this.uuid, this.notes);
    }
}
