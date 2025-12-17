package com.pgalaxyp.fragmento.client.render.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity;
import com.pgalaxyp.fragmento.client.render.model.FluteSpiritModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class FluteSpiritRenderer extends GeoEntityRenderer<BardSpiritEntity> {

    public FluteSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FluteSpiritModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(BardSpiritEntity anim, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void preRender(PoseStack poseStack, BardSpiritEntity animatable, software.bernie.geckolib.cache.object.BakedGeoModel model, MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int red) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red);
    }
}