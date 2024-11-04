package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.block.XenoArtifactBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class SynBlocks {

    public static class Properties {
        public static BlockBehaviour.Properties wood = Block.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS);
    }

    public static final Supplier<Block> XENO_ARTIFACT = registerBlock("xeno_artifact", () -> new XenoArtifactBlock(BlockBehaviour.Properties.of()));

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> supplier = SynRegistry.registerBlock(name, block);
        SynRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()), "tab");
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockHidden(String name, Supplier<T> block) {
        Supplier<T> supplier = SynRegistry.registerBlock(name, block);
        SynRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties()), null);
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockOnly(String name, Supplier<T> block) {
        return SynRegistry.registerBlock(name, block);
    }

    public static void init() {}
}