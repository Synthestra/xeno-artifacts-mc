package com.synthestra.xeno_artifacts.item;

import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import com.synthestra.xeno_artifacts.registry.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class NodeScannerItem extends Item {
    public NodeScannerItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }

        return InteractionResult.CONSUME;
    }

    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 100;
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration >= 0 && livingEntity instanceof Player player) {
            HitResult hitResult = this.calculateHitResult(player);
            if (hitResult instanceof BlockHitResult blockHitResult) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    int i = this.getUseDuration(stack, livingEntity) - remainingUseDuration + 1;
                    if (i % 10 == 0) {
                        BlockPos blockPos = blockHitResult.getBlockPos();

                        if (level.getBlockEntity(blockPos) instanceof XenoArtifactBlockEntity xenoArtifactBE) {
                            level.playSound(player, blockPos, ModSoundEvents.NODE_SCANNER_SCAN.get(), SoundSource.BLOCKS);
                            if (!level.isClientSide && (i == 100 || player.isCreative())) {
                                xenoArtifactBE.finishScan((Player) livingEntity);
                                player.getCooldowns().addCooldown(stack, 100);
                            }

                        }
                    }

                    return;
                }
            }

            livingEntity.releaseUsingItem();
        } else {
            livingEntity.releaseUsingItem();
        }
    }
    private HitResult calculateHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(player, (entity) -> !entity.isSpectator() && entity.isPickable(), player.blockInteractionRange());
    }
}
