package com.synthestra.xeno_artifacts.client.model.fabric;

import com.synthestra.xeno_artifacts.client.model.CustomBakedModel;
import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ModelWrapper implements BakedModel, FabricBakedModel {

    private final CustomBakedModel father;

    @Nullable
    private final ExtraModelData data;

    public ModelWrapper(CustomBakedModel father, @Nullable ExtraModelData data) {
        this.father = father;
        this.data = data;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return father.getBlockQuads(blockState, direction, randomSource, RenderType.cutout(), data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return father.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return father.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return father.usesBlockLight();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return father.getBlockParticle(data);
    }

    @Override
    public ItemTransforms getTransforms() {
        return father.getTransforms();
    }
}