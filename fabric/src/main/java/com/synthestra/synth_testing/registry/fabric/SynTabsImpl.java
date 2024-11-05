package com.synthestra.synth_testing.registry.fabric;

import com.synthestra.synth_testing.SynthTesting;
import com.synthestra.synth_testing.registry.SynBlocks;
import com.synthestra.synth_testing.registry.SynRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SynTabsImpl {
    public static final CreativeModeTab AFM_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            SynthTesting.res("tab"),
            FabricItemGroup.builder().title(Component.translatable("item_group." + SynthTesting.MOD_ID + ".tab"))
            .icon(() -> new ItemStack(SynBlocks.XENO_ARTIFACT.get().asItem())).displayItems((parameters, output) -> {
                output.acceptAll(SynRegistry.getAllModItems());
            }).build());

    public static void register() {}
}
