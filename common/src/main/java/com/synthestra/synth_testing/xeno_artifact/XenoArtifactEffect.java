package com.synthestra.synth_testing.xeno_artifact;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum XenoArtifactEffect implements StringRepresentable {
    SPAWN_ITEM("spawn_item"),
    SPAWN_ENTITY("spawn_entity");

    private final String name;

    XenoArtifactEffect(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
