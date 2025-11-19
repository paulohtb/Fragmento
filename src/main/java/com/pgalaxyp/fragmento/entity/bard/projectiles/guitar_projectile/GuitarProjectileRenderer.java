package com.pgalaxyp.fragmento.entity.bard.projectiles.guitar_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuitarProjectileRenderer extends GeoEntityRenderer<GuitarProjectile> {

    public GuitarProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GuitarProjectileModel());
    }

    @Override
    public void render(GuitarProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}