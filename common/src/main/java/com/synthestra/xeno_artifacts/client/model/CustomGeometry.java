package com.synthestra.xeno_artifacts.client.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;

import java.util.function.Function;

@FunctionalInterface
public interface CustomGeometry {

    BakedModel bake(ModelBakery modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform);

}