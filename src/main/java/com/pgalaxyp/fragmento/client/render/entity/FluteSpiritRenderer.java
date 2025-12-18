package com.pgalaxyp.fragmento.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class FluteSpiritRenderer extends GeoEntityRenderer<com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity> {

    public FluteSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new com.pgalaxyp.fragmento.client.render.model.FluteSpiritModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(
            com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity anim,
            ResourceLocation texture,
            MultiBufferSource bufferSource,
            float partialTick
    ) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity animatable,
            software.bernie.geckolib.cache.object.BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int red
    ) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red);
    }
}