package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> HARDEN_TO_DEEPSLATE = createKey("harden_to_deepslate");

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, XenoArtifacts.res(name));
    }

    public static void init() {}
}
