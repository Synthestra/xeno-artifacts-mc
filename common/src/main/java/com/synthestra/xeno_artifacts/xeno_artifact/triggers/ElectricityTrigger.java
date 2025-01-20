package com.synthestra.xeno_artifacts.xeno_artifact.triggers;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ElectricityTrigger extends Trigger implements ListenerTrigger {

    public String customMessage() {
        return "Electricity is needed";
    }

    public boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (gameEvent.is(GameEvent.LIGHTNING_STRIKE)) {
            if (context.sourceEntity() instanceof LightningBolt lightningBolt) {
                lightningBolt.setVisualOnly(true);
            }
            return true;
        }
        return false;
    }

    @Override
    public int listenRadius() {
        return 8;
    }
}
