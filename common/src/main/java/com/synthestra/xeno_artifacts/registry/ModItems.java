package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.item.*;
import com.synthestra.xeno_artifacts.item.component.NodeScannerPrintOutData;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactDataGenerator;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactScrapbookData;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModItems {

    public static final Supplier<Item> XENO_ARTIFACT = registerBlock(ModBlocks.XENO_ARTIFACT.get(),
            (properties) -> properties.component(ModDataComponents.XENO_ARTIFACT_GENERATOR.get(), new XenoArtifactDataGenerator(3, 8, 3)));

    public static final Supplier<Item> XENO_NODE_BIAS_UP = registerItem("xeno_node_bias_up", (properties) -> new XenoNodeBiasItem(properties, Direction.UP));
    public static final Supplier<Item> XENO_NODE_BIAS_DOWN = registerItem("xeno_node_bias_down", (properties) -> new XenoNodeBiasItem(properties, Direction.DOWN));
    public static final Supplier<Item> ARTIFEXIUM_STAFF = registerItem("artifexium_staff", ArtifexiumStaffItem::new);
    public static final Supplier<Item> NODE_SCANNER = registerItem("node_scanner", NodeScannerItem::new);
    public static final Supplier<Item> NODE_SCANNER_PRINT_OUT = registerItem("node_scanner_print_out", (properties) -> new NodeScannerPrintOutItem(properties.component(ModDataComponents.NODE_SCANNER_DATA.get(), new NodeScannerPrintOutData(null, null))));
    public static final Supplier<Item> XENO_ARTIFACT_NOTEBOOK = registerItem("xeno_artifact_notebook", (properties) -> new XenoArtifactNotebookItem(properties.component(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get(), XenoArtifactScrapbookData.EMPTY)));


    public static Supplier<Item> registerItem(String name, Function<Item.Properties, Item> factory) {
        return registerItem(name, modItemId(name), factory, new Item.Properties());
    }

    public static Supplier<Item> registerItem(String name, ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        //return (Item)Registry.register(BuiltInRegistries.ITEM, key, item);
        return ModRegistry.registerItem(name, () -> item);
    }

    public static Supplier<Item> registerBlock(Block block, Item.Properties properties) {
        return registerBlock(block, BlockItem::new, properties);
    }

    public static Supplier<Item> registerBlock(Block block, UnaryOperator<Item.Properties> propertiesModifier) {
        return registerBlock(block, (blockx, properties) -> new BlockItem(blockx, propertiesModifier.apply(properties)));
    }

    public static Supplier<Item> registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory) {
        return registerBlock(block, factory, new Item.Properties());
    }

    public static Supplier<Item> registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = blockIdToItemId(block.builtInRegistryHolder().key());
        return registerItem(key.location().getPath(), key,
                (propertiesx) -> factory.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }



    private static ResourceKey<Item> modItemId(String name) {
        return ResourceKey.create(Registries.ITEM, XenoArtifacts.res(name));
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> blockId) {
        return ResourceKey.create(Registries.ITEM, blockId.location());
    }

    public static void init() {}
}
