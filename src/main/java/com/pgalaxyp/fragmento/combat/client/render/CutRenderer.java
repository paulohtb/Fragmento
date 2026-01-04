package com.pgalaxyp.fragmento.combat.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.combat.content.entity.CutEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public final class CutRenderer extends EntityRenderer<CutEntity> {

    private static final ResourceLocation WHITE =
            ResourceLocation.withDefaultNamespace("textures/misc/white.png");

    public CutRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            CutEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        if (entity == null) {
            return;
        }

        int life = Math.max(1, entity.lifeTicks());
        float t = (entity.ageTicks() + partialTick) / (float) life;
        t = Mth.clamp(t, 0.0f, 1.0f);

        float alpha = 1.0f - t;
        alpha = alpha * alpha;

        poseStack.pushPose();

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(35.0f));
        poseStack.scale(2.2f, 1.0f, 1.0f);

        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityTranslucent(WHITE));
        Matrix4f m = poseStack.last().pose();

        float w = 0.55f;
        float h = 0.22f;

        float u0 = 0.0f;
        float v0 = 0.0f;
        float u1 = 1.0f;
        float v1 = 1.0f;

        vc.addVertex(m, -w, -h, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha).setUv(u0, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0f, 0.0f, 1.0f);
        vc.addVertex(m,  w, -h, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0f, 0.0f, 1.0f);
        vc.addVertex(m,  w,  h, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha).setUv(u1, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0f, 0.0f, 1.0f);
        vc.addVertex(m, -w,  h, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha).setUv(u0, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0f, 0.0f, 1.0f);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CutEntity entity) {
        return WHITE;
    }
}