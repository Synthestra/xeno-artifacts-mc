package com.synthestra.synth_testing.xeno_artifact.events;

import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;

public class RandomTeleportReaction extends Reaction {
    public RandomTeleportReaction() {}

    public void effect(Level level, BlockPos pos) {
        if (level.isClientSide) return;
        level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16))
                .stream().filter(player -> inLineOfSight(level, pos.getCenter(), player.getEyePosition()))
                .forEach(player -> teleport(level, player));
    }

    public boolean inLineOfSight(Level level, Vec3 pos, Vec3 targetPos) {
        BlockHitResult blockHitResult = level.clip(new ClipContext(targetPos, pos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
        return blockHitResult.getBlockPos().equals(BlockPos.containing(pos)) || blockHitResult.getType() == HitResult.Type.MISS;
    }

    public void teleport(Level level, LivingEntity livingEntity) {
        for(int i = 0; i < 16; ++i) {
            double d = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5) * 16.0;
            double e = Mth.clamp(livingEntity.getY() + (double)(livingEntity.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1);
            double f = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5) * 16.0;
            if (livingEntity.isPassenger()) {
                livingEntity.stopRiding();
            }

            Vec3 vec3 = livingEntity.position();
            if (livingEntity.randomTeleport(d, e, f, true)) {
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(livingEntity));

                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.NEUTRAL);
                livingEntity.resetFallDistance();
                break;
            }
        }
    }
}
