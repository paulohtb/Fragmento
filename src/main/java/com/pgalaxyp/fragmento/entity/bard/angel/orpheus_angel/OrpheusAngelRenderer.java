package com.pgalaxyp.fragmento.entity.bard.angel.orpheus_angel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrpheusAngelRenderer extends GeoEntityRenderer<OrpheusAngel> {

    public OrpheusAngelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OrpheusAngelModel());
    }

    @Override
    public void render(OrpheusAngel entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}