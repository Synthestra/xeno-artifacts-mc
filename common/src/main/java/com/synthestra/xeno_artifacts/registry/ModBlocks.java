package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.block.XenoArtifactBlock;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactDataGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static class Properties {
        public static BlockBehaviour.Properties wood = Block.Properties.of().strength(0.01F, 3.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS);
    }

    public static final Supplier<Block> XENO_ARTIFACT = registerBlock("xeno_artifact",
            (properties) -> new XenoArtifactBlock(properties.strength(5.0F, 1200.0F)));




    public static Supplier<Block> registerBlock(String name, Function<Block.Properties, Block> factory) {
        return registerBlock(name, modItemId(name), factory, Block.Properties.of());
    }

    public static Supplier<Block> registerBlock(String name, ResourceKey<Block> key, Function<Block.Properties, Block> factory, Block.Properties properties) {
        Block block = factory.apply(properties.setId(key));

        return ModRegistry.registerBlock(name, () -> block);
    }

    public static Supplier<Item> registerItem(String name) {
        ResourceLocation id = XenoArtifacts.res(name);
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);

        Item.Properties properties = new Item.Properties()
                .useItemDescriptionPrefix()
                .setId(key);

        return ModRegistry.registerItem(name, () -> new Item(properties));

    }

    private static ResourceKey<Block> modItemId(String name) {
        return ResourceKey.create(Registries.BLOCK, XenoArtifacts.res(name));
    }

//    public static Supplier<Block> registerBlock(String name) {
//        ResourceLocation id = XenoArtifacts.res(name);
//        ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(), id);
//
//        Block.Properties properties = Block.Properties.of()
//                .setId(key);
//
//        return ModRegistry.registerBlock(name, () -> new Block(properties));
//    }


//    public static <T extends Block> Supplier<T> registerBlockItem(String name, Supplier<T> block, Item.Properties properties) {
//        Supplier<T> supplier = ModRegistry.registerBlock(name, block);
//        ModRegistry.registerItem(name, () -> new BlockItem(supplier.get(), properties.setId(modItemId(name))), "tab");
//
//        return supplier;
//    }

    public static void init() {}
}