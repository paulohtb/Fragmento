package com.pgalaxyp.fragmento.entity.bard.projectile.drum_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrumProjectileRenderer extends GeoEntityRenderer<DrumProjectile> {

    public DrumProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrumProjectileModel());
    }

    @Override
    public void render(DrumProjectile entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}