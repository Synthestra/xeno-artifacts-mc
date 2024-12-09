package com.synthestra.synth_testing.xeno_artifact.triggers;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ElectricityTrigger extends Trigger implements ListenerTrigger {

    public String customMessage() {
        return "Electricity is needed";
    }

    public boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        return gameEvent.is(GameEvent.LIGHTNING_STRIKE);
    }

    @Override
    public int listenRadius() {
        return 8;
    }
    //testing repo name change
}
