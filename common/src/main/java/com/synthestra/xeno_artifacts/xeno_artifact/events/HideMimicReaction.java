package com.synthestra.xeno_artifacts.xeno_artifact.events;

import com.synthestra.xeno_artifacts.api.network.NetworkHelper;
import com.synthestra.xeno_artifacts.block.XenoArtifactBlock;
import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import com.synthestra.xeno_artifacts.common.network.ClientBoundParticlePacket;
import com.synthestra.xeno_artifacts.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;

public class HideMimicReaction extends Reaction {
    public HideMimicReaction() {
        super();
    }

    public boolean effect(Level level, BlockPos pos) {
        teleport(level, pos);
        return false;
    }

    private void teleport(Level level, BlockPos pos) {
        WorldBorder worldBorder = level.getWorldBorder();
        for(int i = 0; i < 300; ++i) {
            BlockPos blockPos = pos.offset(level.random.nextInt(16) - level.random.nextInt(16), level.random.nextInt(8) - level.random.nextInt(8), level.random.nextInt(16) - level.random.nextInt(16));
            if (level.getBlockState(blockPos).is(ModBlockTags.MIMICABLE) && worldBorder.isWithinBounds(blockPos)) {
                if (level instanceof ServerLevel serverLevel) NetworkHelper.sendToAllClientPlayersInParticleRange(serverLevel, pos,
                        new ClientBoundParticlePacket(pos, ClientBoundParticlePacket.Kind.MIMIC_TELEPORT, blockPos));

                if (!level.isClientSide) {
                    BlockState state = level.getBlockState(pos);
                    if (level.getBlockEntity(pos) instanceof XenoArtifactBlockEntity xenoArtifactBE) {
                        xenoArtifactBE.setRemoved();

                        BlockState mimicState = level.getBlockState(blockPos);
                        level.setBlock(blockPos, state.setValue(XenoArtifactBlock.HAS_MIMIC, true), 3);
                        if (level.getBlockEntity(blockPos) instanceof XenoArtifactBlockEntity xenoArtifactBE2) {
                            xenoArtifactBE2.setNodeTree(xenoArtifactBE.getNodeTree());
                            xenoArtifactBE2.setUUID(xenoArtifactBE.getUUID());
                            xenoArtifactBE2.setDirectionBias(xenoArtifactBE.getDirectionBias());
                            xenoArtifactBE2.currentNode = xenoArtifactBE.currentNode;
                            xenoArtifactBE2.setCurrentNodeId(xenoArtifactBE.getCurrentNodeId());
                            xenoArtifactBE2.setHeldBlock(mimicState, 0);
                        }

                        level.removeBlockEntity(pos);
                        level.removeBlock(pos, false);
                    }

                }

                return;
            }
        }

    }
}
