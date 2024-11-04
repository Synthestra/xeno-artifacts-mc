package com.synthestra.synth_testing.xeno_artifact;

import net.minecraft.util.StringRepresentable;

public enum XenoArtifactEvents implements StringRepresentable {
    SPAWN_ITEM("spawn_item"),
    SPAWN_ENTITY("spawn_entity");

    private final String name;

    XenoArtifactEvents(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
