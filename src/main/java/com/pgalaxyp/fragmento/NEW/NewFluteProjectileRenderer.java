package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NewFluteProjectileRenderer extends EntityRenderer<NewFluteProjectile> {

    private final NewBanjo<NewFluteProjectile> model;

    public NewFluteProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new NewBanjo<>(ctx.bakeLayer(NewBanjo.LAYER_LOCATION));
    }

    @Override
    public void render(NewFluteProjectile entity, float yaw, float partialTick,
                       PoseStack pose, MultiBufferSource buffer, int light) {

        pose.pushPose();

        pose.scale(1.5f, 1.5f, 1.5f);

        VertexConsumer vertex =
                buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        int overlay = OverlayTexture.NO_OVERLAY;
        int color = 0xFFFFFFFF;

        model.renderToBuffer(pose, vertex, light, overlay, color);

        pose.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NewFluteProjectile entity) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/banjo.png");
    }
}
