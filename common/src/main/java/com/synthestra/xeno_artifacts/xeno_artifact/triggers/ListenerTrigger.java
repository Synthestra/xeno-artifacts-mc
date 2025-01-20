package com.synthestra.xeno_artifacts.xeno_artifact.triggers;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public interface ListenerTrigger {
    boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos);

    int listenRadius();
}
