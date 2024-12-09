package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.xeno_artifact.triggers.ElectricityTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.EntityDieTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.PotionTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.Trigger;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum XenoArtifactTrigger implements StringRepresentable {
    ENTITY_DIE("entity_die", new EntityDieTrigger()),
    ELECTRICITY("electricity", new ElectricityTrigger()),
    BENEFICIAL_POTION("beneficial_potion", new PotionTrigger(MobEffectCategory.BENEFICIAL)),
    HARMFUL_POTION("harmful_potion", new PotionTrigger(MobEffectCategory.HARMFUL));

    private static final XenoArtifactTrigger[] VALUES = values();
    public static final StringRepresentable.EnumCodec<XenoArtifactTrigger> CODEC = StringRepresentable.fromEnum(XenoArtifactTrigger::values);


    private final String name;
    private final Trigger trigger;

    XenoArtifactTrigger(final String name, final Trigger trigger) {
        this.name = name;
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    @Nullable
    public static XenoArtifactTrigger byName(@Nullable String name) {
        return CODEC.byName(name);
    }

    public static XenoArtifactTrigger pick(RandomSource random) {
        int pick = random.nextInt(VALUES.length);
        return VALUES[pick];
    }
}
