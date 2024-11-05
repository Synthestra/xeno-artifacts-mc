package com.synthestra.synth_testing.neoforge;

import com.synthestra.synth_testing.registry.neoforge.SynRegistryImpl;
import com.synthestra.synth_testing.registry.neoforge.SynTabsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.synthestra.synth_testing.SynthTesting;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(SynthTesting.MOD_ID)
public final class SynthTestingNeoForge {
    public SynthTestingNeoForge(IEventBus bus) {
        SynthTesting.init();

        SynRegistryImpl.BLOCKS.register(bus);
        SynRegistryImpl.ITEMS.register(bus);
        SynRegistryImpl.SOUND_EVENTS.register(bus);
        SynRegistryImpl.ENTITY_TYPES.register(bus);
        SynRegistryImpl.BLOCK_ENTITY_TYPES.register(bus);
        SynTabsImpl.register(bus);

        bus.addListener(this::setup);
        //NeoForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            //SynBlocks.registerFlammables();
        });
    }
}
