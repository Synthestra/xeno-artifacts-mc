package com.synthestra.synth_testing.block;

import com.mojang.serialization.MapCodec;
import com.synthestra.synth_testing.block.entity.XenoArtifactBlockEntity;
import com.synthestra.synth_testing.block.properties.SynBlockStateProperties;
import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.registry.SynItems;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNode;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class XenoArtifactBlock extends BaseEntityBlock {
    public static final MapCodec<XenoArtifactBlock> CODEC = simpleCodec(XenoArtifactBlock::new);
    public MapCodec<XenoArtifactBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty ACTIVATED = SynBlockStateProperties.ACTIVATED;

    protected static final VoxelShape AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public XenoArtifactBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state.is(oldState.getBlock())) return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof XenoArtifactBlockEntity xenoArtifactBE) xenoArtifactBE.generateTree();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new XenoArtifactBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, SynBlockEntityTypes.XENO_ARTIFACT.get(), XenoArtifactBlockEntity::serverTick);
    }
}
