package com.synthestra.xeno_artifacts.fabric;

import com.synthestra.xeno_artifacts.XenoArtifactsClient;
import com.synthestra.xeno_artifacts.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class XenoArtifactsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        XenoArtifactsClient.init();
//        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
//                ModBlocks.XENO_ARTIFACT.get()
//        );

        PRE_CLIENT_SETUP_WORK.forEach(Runnable::run);
        //CLIENT_SETUP_WORK.forEach(Runnable::run);
        PRE_CLIENT_SETUP_WORK.clear();
    }

    public static Queue<Runnable> PRE_CLIENT_SETUP_WORK = new ConcurrentLinkedQueue<>();
}
