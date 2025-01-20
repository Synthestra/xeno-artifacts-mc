package com.synthestra.xeno_artifacts;

import com.synthestra.xeno_artifacts.common.network.ModNetwork;
import com.synthestra.xeno_artifacts.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XenoArtifacts {
    public static final String MOD_ID = "xeno_artifacts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ModNetwork.init();
        ModBlocks.init();
        ModItems.init();
        ModSoundEvents.init();
        //SynEntityTypes.init();
        ModBlockEntityTypes.init();

        ModItemTags.init();
        ModBlockTags.init();
        ModDataComponents.init();
    }

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}