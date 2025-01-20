package com.synthestra.xeno_artifacts.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class ClientReceivers {

    private static void withPlayerDo(Consumer<Player> action) {
        var player = Minecraft.getInstance().player;
        if (player != null) action.accept(player);
    }

    private static void withLevelDo(Consumer<Level> action) {
        var level = Minecraft.getInstance().level;
        if (level != null) action.accept(level);
    }

    public static void handleSpawnBlockParticlePacket(ClientBoundParticlePacket message) {
        withLevelDo(l -> {
            final RandomSource ran = l.random;
            switch (message.type) {
                case MIMIC_TELEPORT -> {
                    // same as teleport
                    Vec3 start = message.pos;
                    Vec3 end = message.dir;
                    for(int j = 0; j < 128; ++j) {
                        double d = l.random.nextDouble();
                        float f = (l.random.nextFloat() - 0.5F) * 0.2F;
                        float g = (l.random.nextFloat() - 0.5F) * 0.2F;
                        float h = (l.random.nextFloat() - 0.5F) * 0.2F;
                        double dx = Mth.lerp(d, end.x(), start.x()) + (ran.nextDouble() - 0.5) + 0.5;
                        double dy = Mth.lerp(d, end.y(), start.y()) + ran.nextDouble() - 0.5;
                        double dz = Mth.lerp(d, end.z(), start.z()) + (ran.nextDouble() - 0.5) + 0.5;
                        l.addParticle(ParticleTypes.PORTAL, dx, dy, dz, f, g, h);
                    }
                }
            }
        });

    }
}