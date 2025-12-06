package com.pgalaxyp.fragmento.feature.bard_class.client.render.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.feature.bard_class.client.model.spirit.FluteVortexGeoModel;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl.FluteVortex;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FluteVortexRenderer extends GeoEntityRenderer<FluteVortex> {

    public FluteVortexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FluteVortexGeoModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(FluteVortex animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            FluteVortex animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int color
    ) {
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
