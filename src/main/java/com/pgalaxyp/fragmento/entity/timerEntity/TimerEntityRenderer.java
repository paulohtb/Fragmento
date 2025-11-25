package com.pgalaxyp.fragmento.entity.timerEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TimerEntityRenderer extends GeoEntityRenderer<TimerEntity> {

    public TimerEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TimerEntityModel());
    }

    @Override
    public void render(TimerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}