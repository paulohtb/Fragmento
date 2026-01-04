package com.pgalaxyp.fragmento.rpg.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.CutOrientation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public final class CutEffectRenderer extends EntityRenderer<CutEffectEntity> {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/cut.png");

    public CutEffectRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            CutEffectEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        int life = Math.max(1, entity.lifeTicks());
        float t = (entity.tickCount + partialTick) / life;
        t = Mth.clamp(t, 0.0f, 1.0f);

        float alpha = 1.0f - t;
        alpha *= alpha;

        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        float roll = entity.getOrientation() == CutOrientation.HORIZONTAL ? 90.0f : 0.0f;

        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(roll));

        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityTranslucent(TEX));
        Matrix4f m = poseStack.last().pose();

        float len = 1.8f;
        float wid = 0.45f;

        quad(vc, m, -wid, -len, wid, -len, wid * 0.15f, len, -wid * 0.15f, len, alpha, 15728880);

        poseStack.popPose();
    }

    private static void quad(
            VertexConsumer vc,
            Matrix4f m,
            float x0,
            float y0,
            float x1,
            float y1,
            float x2,
            float y2,
            float x3,
            float y3,
            float alpha,
            int light
    ) {
        float nx = 0.0f;
        float ny = 0.0f;
        float nz = 1.0f;

        vc.addVertex(m, x0, y0, 0)
                .setColor(1, 1, 1, alpha)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(nx, ny, nz);

        vc.addVertex(m, x1, y1, 0)
                .setColor(1, 1, 1, alpha)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(nx, ny, nz);

        vc.addVertex(m, x2, y2, 0)
                .setColor(1, 1, 1, alpha)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(nx, ny, nz);

        vc.addVertex(m, x3, y3, 0)
                .setColor(1, 1, 1, alpha)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(nx, ny, nz);
    }

    @Override
    public ResourceLocation getTextureLocation(CutEffectEntity entity) {
        return TEX;
    }
}