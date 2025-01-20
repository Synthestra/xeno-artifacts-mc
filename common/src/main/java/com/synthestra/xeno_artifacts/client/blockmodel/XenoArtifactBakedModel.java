package com.synthestra.xeno_artifacts.client.blockmodel;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.block.properties.SynBlockStateProperties;
import com.synthestra.xeno_artifacts.client.model.CustomBakedModel;
import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class XenoArtifactBakedModel implements CustomBakedModel {
    private final BakedModel overlay;
    private final BlockModelShaper blockModelShaper;

    public XenoArtifactBakedModel(BakedModel overlay, ModelState state) {
        this.overlay = overlay;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        //always on cutout layer

        List<BakedQuad> quads = new ArrayList<>();
        if (state != null) {
            try {
                BlockState mimic = data.get(SynBlockStateProperties.MIMIC);

                if (mimic == null || mimic.isAir()) {
                    BakedModel model = blockModelShaper.getBlockModel(state);
                    quads.addAll(model.getQuads(mimic, side, rand));
                    return quads;
                }

                BakedModel model = blockModelShaper.getBlockModel(mimic);
                quads.addAll(model.getQuads(mimic, side, rand));

            } catch (Exception ignored) {
            }

            try {
                quads.addAll(overlay.getQuads(state, side, rand));
            } catch (Exception ignored) {
            }
        }
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData extraModelData) {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(XenoArtifacts.res("block/xeno_artifact_side"));
    }


    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }


}