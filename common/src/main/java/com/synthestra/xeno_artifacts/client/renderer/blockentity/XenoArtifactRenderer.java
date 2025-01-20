package com.synthestra.xeno_artifacts.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.synthestra.xeno_artifacts.block.entity.XenoArtifactBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

@Environment(value= EnvType.CLIENT)
public class XenoArtifactRenderer implements BlockEntityRenderer<XenoArtifactBlockEntity> {

    public XenoArtifactRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(XenoArtifactBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

    }
}
