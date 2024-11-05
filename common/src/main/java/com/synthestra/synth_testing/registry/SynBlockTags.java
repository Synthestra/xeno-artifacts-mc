package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.SynthTesting;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SynBlockTags {

    //public static final TagKey<Block> BENCHES = blockTag("benches");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, SynthTesting.res(name));
    }

    public static void init() {}
}
