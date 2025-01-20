package com.synthestra.xeno_artifacts.mixins.fabric;

//import com.synthestra.xeno_artifacts.client.model.CustomGeometry;
//import com.synthestra.xeno_artifacts.client.model.fabric.BlockModelWithCustomGeo;
//import net.minecraft.client.renderer.block.model.BlockModel;
//import net.minecraft.client.renderer.block.model.ItemTransforms;
//import net.minecraft.client.renderer.block.model.TextureSlots;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.client.resources.model.Material;
//import net.minecraft.client.resources.model.ModelBaker;
//import net.minecraft.client.resources.model.ModelState;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.function.Function;
//
//@Mixin(BlockModel.class)
//public class BlockModelMixin {
//
//    @Shadow protected BlockModel parent;
//
//    // makes models with a custom parent loader also use its geometry baking
//    @Inject(at = @At("HEAD"), cancellable = true,
//            method = "bake")
//    void moonlight$bakeCustomGeo(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms, CallbackInfoReturnable<BakedModel> cir) {
//        CustomGeometry geo = getParentGeoRecursive(model);
//        if (geo != null) {
//            cir.setReturnValue(geo.bake(baker, spriteGetter, state));
//        }
//    }
//
//    @Unique
//    private CustomGeometry getParentGeoRecursive(BlockModel model) {
//        if (model != null) {
//            if (model instanceof BlockModelWithCustomGeo w) {
//                return w.getCustomGeometry();
//            }
//
////            if (this.parent != model) {
////                return getParentGeoRecursive(this.parent);
////            }
//
//        }
//        return null;
//    }
//
//
//}