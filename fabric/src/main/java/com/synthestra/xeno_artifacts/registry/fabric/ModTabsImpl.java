package com.synthestra.xeno_artifacts.registry.fabric;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.registry.ModBlocks;
import com.synthestra.xeno_artifacts.registry.ModRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ModTabsImpl {
    public static final CreativeModeTab XENO_ARTIFACTS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            XenoArtifacts.res("tab"),
            FabricItemGroup.builder().title(Component.translatable("item_group." + XenoArtifacts.MOD_ID + ".tab"))
            .icon(() -> new ItemStack(Blocks.STONE)).displayItems((parameters, output) -> {
                output.acceptAll(ModRegistry.getAllModItems());
            }).build());

    public static void register() {}
}
