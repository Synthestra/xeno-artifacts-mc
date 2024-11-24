package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.SynthTesting;
import com.synthestra.synth_testing.item.ArtifexiumStaffItem;
import com.synthestra.synth_testing.item.XenoNodeBiasItem;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class SynConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> HARDEN_TO_DEEPSLATE = createKey("harden_to_deepslate");

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, SynthTesting.res(name));
    }

    public static void init() {}
}
