package com.synthestra.synth_testing.registry;

import com.synthestra.synth_testing.SynthTesting;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SynItemTags {

    //public static final TagKey<Item> BENCHES = itemTag("benches");

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, SynthTesting.res(name));
    }

    public static void init() {}
}
