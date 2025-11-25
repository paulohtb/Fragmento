package com.pgalaxyp.fragmento.entity.bard.projectile.lute_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LuteProjectileRenderer extends GeoEntityRenderer<LuteProjectile> {

    public LuteProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LuteProjectileModel());
    }

    @Override
    public void render(LuteProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}