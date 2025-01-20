package com.synthestra.xeno_artifacts.block;

import com.mojang.serialization.MapCodec;
import com.synthestra.xeno_artifacts.api.block.IBlockHolder;
import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import com.synthestra.xeno_artifacts.block.properties.SynBlockStateProperties;
import com.synthestra.xeno_artifacts.registry.ModBlockEntityTypes;
import com.synthestra.xeno_artifacts.xeno_artifact.triggers.ItemUseOnTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class XenoArtifactBlock extends BaseEntityBlock {
    public static final MapCodec<XenoArtifactBlock> CODEC = simpleCodec(XenoArtifactBlock::new);
    public MapCodec<XenoArtifactBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty ACTIVATED = SynBlockStateProperties.ACTIVATED;
    public static final BooleanProperty HAS_MIMIC = SynBlockStateProperties.HAS_MIMIC;

    protected static final VoxelShape AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public XenoArtifactBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, false).setValue(HAS_MIMIC, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HAS_MIMIC, false);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HAS_MIMIC)) return Shapes.block();
        return AABB;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HAS_MIMIC)) return Shapes.block();
        return AABB;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (level.getBlockEntity(pos) instanceof XenoArtifactBlockEntity xenoArtifactBE) {
            if (stack.is(Items.STICK)) {
                if (level.isClientSide) return InteractionResult.SUCCESS;
                xenoArtifactBE.printTree(xenoArtifactBE.getRootNode(), "");
                return InteractionResult.SUCCESS;
            }

            if (stack.getItem() instanceof BlockItem blockItem) {
                BlockState defaultState = blockItem.getBlock().defaultBlockState();
                if (!Block.isShapeFullBlock(defaultState.getOcclusionShape())) return InteractionResult.PASS;
                if (xenoArtifactBE.getHeldBlock(0).is(defaultState.getBlock())) return InteractionResult.PASS;

                xenoArtifactBE.setHeldBlock(defaultState, 0);
                level.setBlock(pos, state.setValue(HAS_MIMIC, true), 3);
                return InteractionResult.SUCCESS;
            }

            if (level.isClientSide || xenoArtifactBE.isSuppressed()) return InteractionResult.PASS;
            if (xenoArtifactBE.getCurrentNode().getTriggerValue() instanceof ItemUseOnTrigger trigger) {
                if (trigger.testItem(stack, player)) {
                    xenoArtifactBE.activateNode(level, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {

        // if block replaced with non mimic variant, clear held block nbt
        if (oldState.is(state.getBlock()) && !state.getValue(HAS_MIMIC)) {
            if (level.getBlockEntity(pos) instanceof XenoArtifactBlockEntity xenoArtifactBE) xenoArtifactBE.setHeldBlock(Blocks.AIR.defaultBlockState());
        }
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
        builder.add(ACTIVATED, HAS_MIMIC);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntityTypes.XENO_ARTIFACT.get(), XenoArtifactBlockEntity::serverTick);
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof XenoArtifactBlockEntity xenoArtifactBE) {
            BlockState mimicState = xenoArtifactBE.getHeldBlock();
            if (!mimicState.isAir() && !(mimicState.getBlock() instanceof XenoArtifactBlock)) {
                return Math.max(super.getDestroyProgress(state, player, level, pos),
                        mimicState.getDestroyProgress(player, level, pos));
            }
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(HAS_MIMIC)) {
            level.setBlock(pos, state.setValue(HAS_MIMIC, false), 3);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getValue(HAS_MIMIC)) {
            return;
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (!state.getValue(HAS_MIMIC)) return drops;
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof IBlockHolder tile) {
            //checks again if the content itself can be mined
            BlockState heldState = tile.getHeldBlock();
            return heldState.getDrops(builder);
        }
        return drops;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HAS_MIMIC)) {
            spawnParticles(level, pos);
        }

    }

    private static void spawnParticles(Level level, BlockPos pos) {
        double d = 0.5625;
        RandomSource randomSource = level.random;
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            BlockPos blockPos = pos.relative(direction);
            if (!level.getBlockState(blockPos).isSolidRender()) {
                Direction.Axis axis = direction.getAxis();
                double e = axis == Direction.Axis.X ? 0.5 + d * (double) direction.getStepX() : (double) randomSource.nextFloat();
                double f = axis == Direction.Axis.Y ? 0.5 + d * (double) direction.getStepY() : (double) randomSource.nextFloat();
                double g = axis == Direction.Axis.Z ? 0.5 + d * (double) direction.getStepZ() : (double) randomSource.nextFloat();
                level.addParticle(DustParticleOptions.REDSTONE, (double) pos.getX() + e, (double) pos.getY() + f, (double) pos.getZ() + g, 0.0, 0.0, 0.0);
            }
        }

    }
}
