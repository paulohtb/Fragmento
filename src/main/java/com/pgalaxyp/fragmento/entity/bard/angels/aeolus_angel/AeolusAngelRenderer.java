package com.pgalaxyp.fragmento.entity.bard.angels.aeolus_angel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AeolusAngelRenderer extends GeoEntityRenderer<AeolusAngel> {

    public AeolusAngelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AeolusAngelModel());
    }

    @Override
    public void render(AeolusAngel entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}