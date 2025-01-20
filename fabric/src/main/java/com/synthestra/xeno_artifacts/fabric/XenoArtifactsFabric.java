package com.synthestra.xeno_artifacts.fabric;

import com.synthestra.xeno_artifacts.registry.fabric.ModTabsImpl;
import net.fabricmc.api.ModInitializer;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public final class XenoArtifactsFabric implements ModInitializer {

    private static MinecraftServer currentServer;
    @Override
    public void onInitialize() {
        XenoArtifacts.init();
        ModTabsImpl.register();


        ServerLifecycleEvents.SERVER_STARTING.register(server -> currentServer = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }
}
