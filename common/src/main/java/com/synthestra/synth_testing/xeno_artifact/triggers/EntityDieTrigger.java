package com.synthestra.synth_testing.xeno_artifact.triggers;

import net.minecraft.Optionull;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityDieTrigger extends Trigger implements ListenerTrigger {

    public String customMessage() {
        return "An Entity needs to die";
    }

    public boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (!gameEvent.is(GameEvent.ENTITY_DIE)) return false;
        if (!(context.sourceEntity() instanceof LivingEntity livingEntity)) return false;
        DamageSource damageSource = livingEntity.getLastDamageSource();
        int i = livingEntity.getExperienceReward(level, Optionull.map(damageSource, DamageSource::getEntity));
        if (livingEntity.shouldDropExperience() && i > 0) {
            livingEntity.skipDropExperience();
            return true;
        }
        return false;
    }

    @Override
    public int listenRadius() {
        return 8;
    }
}
