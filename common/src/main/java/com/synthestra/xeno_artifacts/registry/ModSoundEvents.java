package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final Supplier<SoundEvent> ARTIFACT_ACTIVATE = register("block.xeno_artifact.activate");
    public static final Supplier<SoundEvent> NODE_SCANNER_SCAN = register("item.node_scanner.scan");

    public static Supplier<SoundEvent> register(String name) {
        return ModRegistry.registerSoundEvent(name, () -> SoundEvent.createVariableRangeEvent(XenoArtifacts.res(name)));
    }

    public static void init() {}
}
