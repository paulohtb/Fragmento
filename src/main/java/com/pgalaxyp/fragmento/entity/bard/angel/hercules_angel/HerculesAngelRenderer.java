package com.pgalaxyp.fragmento.entity.bard.angel.hercules_angel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HerculesAngelRenderer extends GeoEntityRenderer<HerculesAngel> {

    public HerculesAngelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HerculesAngelModel());
    }

    @Override
    public void render(HerculesAngel entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}