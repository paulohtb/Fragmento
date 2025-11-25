package com.pgalaxyp.fragmento.entity.bard.angel.hipnos_angel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HipnosAngelRenderer extends GeoEntityRenderer<HipnosAngel> {

    public HipnosAngelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HipnosAngelModel());
    }

    @Override
    public void render(HipnosAngel entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}