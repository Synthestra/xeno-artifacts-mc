package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.xeno_artifact.triggers.ElectricityTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.EntityDieTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.XenoArtifactTrigger;
import net.minecraft.util.StringRepresentable;

public enum XenoArtifactTriggers implements StringRepresentable {
    ENTITY_DIE("entity_die", new EntityDieTrigger()),
    ELECTRICITY("electricity", new ElectricityTrigger());

    private final String name;
    private final XenoArtifactTrigger trigger;

    XenoArtifactTriggers(final String name, final XenoArtifactTrigger trigger) {
        this.name = name;
        this.trigger = trigger;
    }

    public XenoArtifactTrigger getTrigger() {
        return trigger;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
