package com.synthestra.xeno_artifacts.mixins.fabric;

//import com.synthestra.xeno_artifacts.api.block.IExtraModelDataProvider;
//import com.synthestra.xeno_artifacts.client.model.CustomBakedModel;
//import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
//import com.synthestra.xeno_artifacts.client.model.fabric.ModelWrapper;
//import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
//import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.BlockAndTintGetter;
//import net.minecraft.world.level.block.state.BlockState;
//import org.spongepowered.asm.mixin.Mixin;
//
//import java.util.function.Supplier;
//
//@Mixin(CustomBakedModel.class)
//public interface SelfCustomBakedModel extends FabricBakedModel, CustomBakedModel, BakedModel {
//
//    @Override
//    default boolean isVanillaAdapter() {
//        return false;
//    }
//
//    @Override
//    default void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
//        // Object attachment = ((RenderAttachedBlockView)blockView).getBlockEntityRenderAttachment(pos);
//        var tile = blockView.getBlockEntity(pos);
//        ExtraModelData data = ExtraModelData.EMPTY;
//        if (tile instanceof IExtraModelDataProvider provider) {
//            //SlaveModel inner = SlaveModel.INSTANCE;
//            //creating a new instance because indium doesn't like it...
//            data = provider.getExtraModelData();
//        }
//
//        ModelWrapper inner = new ModelWrapper(this, getModelData(data, pos, state, blockView));
//        inner.emitBlockQuads(blockView, state, pos, randomSupplier, context);
//    }
//
//    @Override
//    default void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
//        // Object attachment = ((RenderAttachedBlockView)blockView).getBlockEntityRenderAttachment(pos);
//        ExtraModelData data = ExtraModelData.EMPTY;
//
//        ModelWrapper inner = new ModelWrapper(this, getModelData(data, stack));
//        inner.emitItemQuads(stack,randomSupplier, context);
//    }
//
//
//
//
//}