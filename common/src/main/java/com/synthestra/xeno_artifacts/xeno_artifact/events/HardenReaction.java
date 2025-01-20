package com.synthestra.xeno_artifacts.xeno_artifact.events;

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

    public boolean effect(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) return true;
        serverLevel.registryAccess().get(Registries.CONFIGURED_FEATURE).flatMap(
                (registry) -> registry.value().get(this.hardenTo))
                .ifPresent((reference) -> reference.value().place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, pos));
        return true;
    }
}
