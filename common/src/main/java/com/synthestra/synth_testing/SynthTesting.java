package com.synthestra.synth_testing;

import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.registry.SynBlocks;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SynthTesting {
    public static final String MOD_ID = "synth_testing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        SynBlocks.init();
        //SynItems.init();
        //SynSoundEvents.init();
        //SynEntityTypes.init();
        SynBlockEntityTypes.init();

        //SynItemTags.init();
        //SynBlockTags.init();
    }

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}