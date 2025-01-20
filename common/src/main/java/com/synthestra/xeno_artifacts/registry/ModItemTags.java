package com.synthestra.xeno_artifacts.registry;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    //public static final TagKey<Item> BENCHES = itemTag("benches");

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, XenoArtifacts.res(name));
    }

    public static void init() {}
}
