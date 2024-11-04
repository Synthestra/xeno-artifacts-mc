package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.block.entity.XenoArtifactBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class SynBlockEntityTypes {
    public static final Supplier<BlockEntityType<XenoArtifactBlockEntity>> XENO_ARTIFACT = SynRegistry.registerBlockEntityType("xeno_artifact",
            () -> SynRegistry.createBlockEntityType(XenoArtifactBlockEntity::new,
                    SynBlocks.XENO_ARTIFACT.get()
            ));

    public static void init() {}
}
