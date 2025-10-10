package com.pgalaxyp.fragmento.entity.violinProjectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ViolinProjectileRenderer extends GeoEntityRenderer<ViolinProjectile> {

    public ViolinProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ViolinProjectileModel());
    }

    @Override
    public void render(ViolinProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}