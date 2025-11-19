package com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FluteProjectileRenderer extends GeoEntityRenderer<FluteProjectile> {

    public FluteProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FluteProjectileModel());
    }

    @Override
    public void render(FluteProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}