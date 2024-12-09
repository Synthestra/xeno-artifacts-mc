package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.SynthTesting;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class SynSoundEvents {
    public static final Supplier<SoundEvent> ARTIFACT_ACTIVATE = register("block.xeno_artifact.activate");
    public static final Supplier<SoundEvent> NODE_SCANNER_SCAN = register("item.node_scanner.scan");

    public static Supplier<SoundEvent> register(String name) {
        return SynRegistry.registerSoundEvent(name, () -> SoundEvent.createVariableRangeEvent(SynthTesting.res(name)));
    }

    public static void init() {}
}
