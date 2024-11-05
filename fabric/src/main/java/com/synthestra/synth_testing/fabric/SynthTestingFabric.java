package com.synthestra.synth_testing.fabric;

import com.synthestra.synth_testing.registry.fabric.SynTabsImpl;
import net.fabricmc.api.ModInitializer;

import com.synthestra.synth_testing.SynthTesting;

public final class SynthTestingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SynthTesting.init();
        SynTabsImpl.register();
    }
}
