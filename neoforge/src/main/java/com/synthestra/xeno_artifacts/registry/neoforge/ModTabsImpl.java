package com.synthestra.xeno_artifacts.registry.neoforge;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.registry.ModBlocks;
import com.synthestra.xeno_artifacts.registry.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class ModTabsImpl {
    public static final Supplier<CreativeModeTab> AFM_TAB = ModRegistryImpl.MOD_TABS.register(XenoArtifacts.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModBlocks.XENO_ARTIFACT.get().asItem()))
            .title(Component.translatable("item_group." + XenoArtifacts.MOD_ID + ".tab"))
            .displayItems(((parameters, output) -> {
                output.acceptAll(ModRegistry.getAllModItems());
            })).build());

    public static void register(IEventBus eventBus) {
        ModRegistryImpl.MOD_TABS.register(eventBus);
    }
}
