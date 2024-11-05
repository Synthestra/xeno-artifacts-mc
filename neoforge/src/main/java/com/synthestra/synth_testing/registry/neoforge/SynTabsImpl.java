package com.synthestra.synth_testing.registry.neoforge;

import com.synthestra.synth_testing.SynthTesting;
import com.synthestra.synth_testing.registry.SynBlocks;
import com.synthestra.synth_testing.registry.SynRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class SynTabsImpl {
    public static final Supplier<CreativeModeTab> AFM_TAB = SynRegistryImpl.MOD_TABS.register(SynthTesting.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(SynBlocks.XENO_ARTIFACT.get().asItem()))
            .title(Component.translatable("item_group." + SynthTesting.MOD_ID + ".tab"))
            .displayItems(((parameters, output) -> {
                output.acceptAll(SynRegistry.getAllModItems());
            })).build());

    public static void register(IEventBus eventBus) {
        SynRegistryImpl.MOD_TABS.register(eventBus);
    }
}
