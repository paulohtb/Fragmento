package com.pgalaxyp.fragmento.feature.bard.client.render.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.feature.bard.client.model.spirit.FluteSpiritGeoModel;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.impl.FluteSpirit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FluteSpiritRenderer extends GeoEntityRenderer<FluteSpirit> {

    public FluteSpiritRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FluteSpiritGeoModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(FluteSpirit animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            FluteSpirit animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getXRot()));

        super.preRender(
                poseStack,
                animatable,
                model,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                color
        );
    }

}
