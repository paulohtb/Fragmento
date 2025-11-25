package com.pgalaxyp.fragmento.entity.bard.projectile.lyre_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LyreProjectileRenderer extends GeoEntityRenderer<LyreProjectile> {

    public LyreProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LyreProjectileModel());
    }

    @Override
    public void render(LyreProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}