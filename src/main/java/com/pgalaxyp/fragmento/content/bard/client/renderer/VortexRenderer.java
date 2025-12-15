package com.pgalaxyp.fragmento.content.bard.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.content.bard.client.model.VortexModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class VortexRenderer<T extends Entity & GeoAnimatable> extends GeoEntityRenderer<T> {

    private static final float BASE_HITBOX = 1.5f;

    public VortexRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VortexModel<>());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(
            T anim,
            ResourceLocation texture,
            MultiBufferSource bufferSource,
            float partialTick
    ) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            T animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int red
    ) {
        float w = animatable.getBbWidth();
        float h = animatable.getBbHeight();

        float sW = w > 0.0f ? (w / BASE_HITBOX) : 1.0f;
        float sH = h > 0.0f ? (h / BASE_HITBOX) : 1.0f;

        float s = Math.max(sW, sH);

        poseStack.scale(s, s, s);

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
                red
        );
    }
}
