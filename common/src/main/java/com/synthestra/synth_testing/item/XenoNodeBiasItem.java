package com.synthestra.synth_testing.item;

import com.synthestra.synth_testing.block.entity.XenoArtifactBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
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
                xenoArtifactBE.setDirectionBias(this.biasDirection);
                context.getItemInHand().shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useOn(context);
    }
}
