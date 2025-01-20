package com.synthestra.xeno_artifacts.client.model.fabric;

import com.synthestra.xeno_artifacts.client.model.CustomGeometry;
import com.synthestra.xeno_artifacts.mixins.fabric.BlockModelAccessor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;

import java.util.List;
import java.util.Map;

public class BlockModelWithCustomGeo extends BlockModel {

    private final CustomGeometry geometry;

    public BlockModelWithCustomGeo(CustomGeometry geometry) {
        super(null, List.of(), null, true, null,
                ItemTransforms.NO_TRANSFORMS);
        this.geometry = geometry;
    }

    public BlockModelWithCustomGeo(BlockModel original, CustomGeometry geometry) {
        super(((BlockModelAccessor) original).getParentLocation(), List.of(),
                ((BlockModelAccessor) original).getTextureSlots(), original.getAmbientOcclusion(),
                original.getGuiLight(), original.getTransforms());
        this.geometry = geometry;
    }

    public CustomGeometry getCustomGeometry() {
        return this.geometry;
    }

}