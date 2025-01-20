package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntityTypes {
    public static final Supplier<BlockEntityType<XenoArtifactBlockEntity>> XENO_ARTIFACT = ModRegistry.registerBlockEntityType("xeno_artifact",
            () -> ModRegistry.createBlockEntityType(XenoArtifactBlockEntity::new,
                    ModBlocks.XENO_ARTIFACT.get()
            ));

    public static void init() {}
}
