package com.pgalaxyp.fragmento.entity.bard.angel.apollo_angel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ApolloAngelRenderer extends GeoEntityRenderer<ApolloAngel> {

    public ApolloAngelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ApolloAngelModel());
    }

    @Override
    public void render(ApolloAngel entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}