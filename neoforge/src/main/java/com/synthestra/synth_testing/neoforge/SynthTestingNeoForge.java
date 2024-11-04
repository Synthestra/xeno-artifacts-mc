package com.synthestra.synth_testing.neoforge;

import net.neoforged.fml.common.Mod;

import com.synthestra.synth_testing.SynthTesting;

@Mod(SynthTesting.MOD_ID)
public final class SynthTestingNeoForge {
    public SynthTestingNeoForge() {
        // Run our common setup.
        SynthTesting.init();
    }
}
