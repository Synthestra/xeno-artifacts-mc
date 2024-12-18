package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.item.ArtifexiumStaffItem;
import com.synthestra.synth_testing.item.NodeScannerItem;
import com.synthestra.synth_testing.item.XenoNodeBiasItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SynItems {

    public static final Supplier<Item> XENO_NODE_BIAS_UP = SynRegistry.registerItem("xeno_node_bias_up", () -> new XenoNodeBiasItem(new Item.Properties(), Direction.UP));
    public static final Supplier<Item> XENO_NODE_BIAS_DOWN = SynRegistry.registerItem("xeno_node_bias_down", () -> new XenoNodeBiasItem(new Item.Properties(), Direction.DOWN));
    public static final Supplier<Item> ARTIFEXIUM_STAFF = SynRegistry.registerItem("artifexium_staff", () -> new ArtifexiumStaffItem(new Item.Properties()));
    public static final Supplier<Item> NODE_SCANNER = SynRegistry.registerItem("node_scanner", () -> new NodeScannerItem(new Item.Properties()));


    public static void init() {}
}
