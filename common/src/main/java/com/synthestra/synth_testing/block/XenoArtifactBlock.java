package com.synthestra.synth_testing.block;

import com.mojang.serialization.MapCodec;
import com.synthestra.synth_testing.block.entity.XenoArtifactBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class XenoArtifactBlock extends BaseEntityBlock {
    public static final MapCodec<XenoArtifactBlock> CODEC = simpleCodec(XenoArtifactBlock::new);
    public MapCodec<XenoArtifactBlock> codec() {
        return CODEC;
    }

    public XenoArtifactBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof XenoArtifactBlockEntity xenoArtifactBE) {
            xenoArtifactBE.generateTree();
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new XenoArtifactBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
