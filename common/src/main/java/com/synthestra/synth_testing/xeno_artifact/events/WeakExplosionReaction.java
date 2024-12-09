package com.synthestra.synth_testing.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class WeakExplosionReaction extends Reaction {
    public WeakExplosionReaction() {
        super(10);
    }

    public void effect(Level level, BlockPos pos) {
        if (level.isClientSide) return;

        level.explode(null, Explosion.getDefaultDamageSource(level, null), null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, false, Level.ExplosionInteraction.NONE);
    }
}
