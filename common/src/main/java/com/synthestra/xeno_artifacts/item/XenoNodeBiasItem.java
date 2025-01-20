package com.synthestra.xeno_artifacts.item;

import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class XenoNodeBiasItem extends Item {
    Direction biasDirection;

    public XenoNodeBiasItem(Properties properties, Direction biasDirection) {
        super(properties);
        this.biasDirection = biasDirection;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof XenoArtifactBlockEntity xenoArtifactBE) {
            Direction directionBias = xenoArtifactBE.getDirectionBias();
            if (directionBias != this.biasDirection) {
                if (level.isClientSide) return InteractionResult.SUCCESS;
                xenoArtifactBE.setDirectionBias(this.biasDirection);
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }
}
