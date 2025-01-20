package com.synthestra.xeno_artifacts;

//import com.synthestra.xeno_artifacts.client.blockmodel.XenoArtifactBakedModel;
//import com.synthestra.xeno_artifacts.client.model.CustomBakedModel;
//import com.synthestra.xeno_artifacts.client.model.CustomModelLoader;
//import com.synthestra.xeno_artifacts.client.model.NestedModelLoader;
import com.synthestra.xeno_artifacts.client.renderer.blockentity.XenoArtifactRenderer;
import com.synthestra.xeno_artifacts.registry.ModBlockEntityTypes;
import com.synthestra.xeno_artifacts.registry.ModRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class XenoArtifactsClient {

    public static void init() {
        //ModRegistry.addModelLoaderRegistration(XenoArtifactsClient::registerModelLoaders);

        ModRegistry.registerBlockEntityRenderer(ModBlockEntityTypes.XENO_ARTIFACT, XenoArtifactRenderer::new);
    }

//    private static void registerModelLoaders(ModelLoaderEvent event) {
//        event.register(XenoArtifacts.res("xeno_artifact"), new NestedModelLoader("overlay", XenoArtifactBakedModel::new));
//    }
//
//    @FunctionalInterface
//    public interface ModelLoaderEvent {
//        void register(ResourceLocation id, CustomModelLoader loader);
//
//        default void register(ResourceLocation id, Supplier<CustomBakedModel> bakedModelFactory) {
//            register(id, (CustomModelLoader) (json, context) -> (modelBaker, spriteGetter, transform) -> bakedModelFactory.get());
//        }
//
//        default void register(ResourceLocation id, BiFunction<ModelState, Function<Material, TextureAtlasSprite>, CustomBakedModel> bakedModelFactory) {
//            register(id, (CustomModelLoader) (json, context) -> (modelBaker, spriteGetter, transform) -> bakedModelFactory.apply(transform, spriteGetter));
//        }
//    }
}
