package com.synthestra.xeno_artifacts.item;

import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ArtifexiumStaffItem extends Item {
    public ArtifexiumStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof XenoArtifactBlockEntity xenoArtifactBE) {
            //if (level.isClientSide) return InteractionResult.SUCCESS;
            xenoArtifactBE.activateNode(level, context.getClickedPos());
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
