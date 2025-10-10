package com.pgalaxyp.fragmento.entity.lira_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LiraProjectileRenderer extends GeoEntityRenderer<LiraProjectile> {

    public LiraProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LiraProjectileModel());
    }

    @Override
    public void render(LiraProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}