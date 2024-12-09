package com.synthestra.synth_testing.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class HardenReaction extends Reaction {
    private final ResourceKey<ConfiguredFeature<?,?>> hardenTo;
    RandomSource random = RandomSource.create();
    public HardenReaction(ResourceKey<ConfiguredFeature<?, ?>> hardenTo) {
        super();
        this.hardenTo = hardenTo;
    }

    public void effect(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        serverLevel.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap(
                (registry) -> registry.getHolder(this.hardenTo))
                .ifPresent((reference) -> reference.value().place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, pos));
    }
}
