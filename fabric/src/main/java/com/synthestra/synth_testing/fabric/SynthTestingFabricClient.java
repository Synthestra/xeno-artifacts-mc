package com.synthestra.synth_testing.fabric;

import com.synthestra.synth_testing.registry.SynBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public final class SynthTestingFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                SynBlocks.XENO_ARTIFACT.get()
        );
    }
}
