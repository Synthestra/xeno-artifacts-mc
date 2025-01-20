package com.synthestra.xeno_artifacts.registry;

import com.mojang.serialization.Codec;
import com.synthestra.xeno_artifacts.item.component.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final Supplier<DataComponentType<XenoArtifactData>> XENO_ARTIFACT = register("xeno_artifact",
            XenoArtifactData.CODEC, XenoArtifactData.STREAM_CODEC, true);

    public static final Supplier<DataComponentType<XenoArtifactDataGenerator>> XENO_ARTIFACT_GENERATOR = register("xeno_artifact_generator",
            XenoArtifactDataGenerator.CODEC);

    public static final Supplier<DataComponentType<NodeScannerPrintOutData>> NODE_SCANNER_DATA = register("node_scanner_data",
            NodeScannerPrintOutData.CODEC);

    public static final Supplier<DataComponentType<XenoArtifactScrapbookData>> XENO_ARTIFACT_SCRAPBOOK_DATA = register("xeno_artifact_scrapbook_data",
            XenoArtifactScrapbookData.CODEC, XenoArtifactScrapbookData.STREAM_CODEC, true);

    public static final Supplier<DataComponentType<XenoArtifactScrapbookPage>> XENO_ARTIFACT_SCRAPBOOK_PAGE = register("xeno_artifact_scrapbook_page",
            XenoArtifactScrapbookPage.CODEC, XenoArtifactScrapbookPage.STREAM_CODEC, true);

    public static <T> Supplier<DataComponentType<T>> register(String name, Supplier<DataComponentType<T>> factory) {
        return ModRegistry.registerDataComponent(name, factory);
    }

    public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec,
                                                              @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec,
                                                              boolean cache) {
        return ModRegistry.registerDataComponent(name, () -> {
            var builder = DataComponentType.<T>builder()
                    .persistent(codec);
            if (streamCodec != null) builder.networkSynchronized(streamCodec);
            if (cache) builder.cacheEncoding();
            return builder.build();
        });
    }

    public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return register(name, codec, streamCodec, false);
    }

    public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec) {
        return register(name, codec, null);
    }

    public static void init() {}
}
