package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> MIMICABLE = blockTag("mimicable");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, XenoArtifacts.res(name));
    }

    public static void init() {}
}
