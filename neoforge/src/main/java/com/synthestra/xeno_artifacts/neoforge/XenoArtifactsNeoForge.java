package com.synthestra.xeno_artifacts.neoforge;

import com.synthestra.xeno_artifacts.registry.neoforge.ModRegistryImpl;
import com.synthestra.xeno_artifacts.registry.neoforge.ModTabsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(XenoArtifacts.MOD_ID)
public final class XenoArtifactsNeoForge {
    public XenoArtifactsNeoForge(IEventBus bus) {
        XenoArtifacts.init();

        ModRegistryImpl.BLOCKS.register(bus);
        ModRegistryImpl.ITEMS.register(bus);
        ModRegistryImpl.SOUND_EVENTS.register(bus);
        ModRegistryImpl.ENTITY_TYPES.register(bus);
        ModRegistryImpl.BLOCK_ENTITY_TYPES.register(bus);
        ModTabsImpl.register(bus);

        bus.addListener(this::setup);
        //NeoForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            //SynBlocks.registerFlammables();
        });
    }
}
