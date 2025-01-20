package com.synthestra.xeno_artifacts.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class WeakExplosionReaction extends Reaction {
    public WeakExplosionReaction() {
        super(10);
    }

    public boolean effect(Level level, BlockPos pos) {
        if (level.isClientSide) return true;

        level.explode(null, Explosion.getDefaultDamageSource(level, null), null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, false, Level.ExplosionInteraction.NONE);
        return true;
    }
}
