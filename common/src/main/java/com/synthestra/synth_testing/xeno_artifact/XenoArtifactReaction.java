package com.synthestra.synth_testing.xeno_artifact;

import com.synthestra.synth_testing.registry.SynConfiguredFeatures;
import com.synthestra.synth_testing.registry.SynLootTables;
import com.synthestra.synth_testing.xeno_artifact.events.*;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum XenoArtifactReaction implements StringRepresentable {
    SPAWN_ITEM("spawn_item", new SpawnItemReaction(new ItemStack(Items.DIAMOND))),
    SPAWN_ENTITY("spawn_entity", new SpawnEntityReaction()),
    HARDEN_TO_DEEPSLATE("harden_to_deepslate", new HardenReaction(SynConfiguredFeatures.HARDEN_TO_DEEPSLATE)),
    RANDOM_TELEPORT("random_teleport", new RandomTeleportReaction()),
    SPAWN_LOOT("spawn_loot", new SpawnLootReaction(SynLootTables.REWARD_1)),
    WEAK_EXPLOSION("weak_explosion", new WeakExplosionReaction());


    private static final XenoArtifactReaction[] VALUES = values();
    public static final StringRepresentable.EnumCodec<XenoArtifactReaction> CODEC = StringRepresentable.fromEnum(XenoArtifactReaction::values);


    private final String name;
    private final Reaction reaction;

    XenoArtifactReaction(final String name, final Reaction reaction) {
        this.name = name;
        this.reaction = reaction;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    @Nullable
    public static XenoArtifactReaction byName(@Nullable String name) {
        return CODEC.byName(name);
    }

    public static XenoArtifactReaction pick(RandomSource random) {
        int pick = random.nextInt(VALUES.length);
        return VALUES[pick];
    }
}
