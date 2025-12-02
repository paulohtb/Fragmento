package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NewFluteSpecialRenderer extends EntityRenderer<NewFluteSpecialEntity> {

    private final NewFluteSpecialModel<NewFluteSpecialEntity> model;

    public NewFluteSpecialRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new NewFluteSpecialModel<>(ctx.bakeLayer(NewFluteSpecialModel.LAYER_LOCATION));
    }

    @Override
    public void render(NewFluteSpecialEntity entity, float yaw, float partialTicks,
                       PoseStack pose, MultiBufferSource buffer, int light) {

        pose.pushPose();

        float scale = entity.getRadius();
        pose.scale(scale, 1.0f, scale);

        model.renderToBuffer(
                pose,
                buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity))),
                light,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        pose.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NewFluteSpecialEntity entity) {
        return ResourceLocation.fromNamespaceAndPath
                ("fragmento", "textures/entity/new_flute_cast_texture.png");
    }
}
