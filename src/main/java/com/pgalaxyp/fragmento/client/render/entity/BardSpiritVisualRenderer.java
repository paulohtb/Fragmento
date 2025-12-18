package com.pgalaxyp.fragmento.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class BardSpiritVisualRenderer {

    private static final ResourceLocation VORTEX_TEX =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/wind_vortex.png");

    private static final float HOVER_RADIUS = 2.2f;
    private static final float BURST_RADIUS = 2.8f;

    private static final float GROUND_EPS = 0.02f;

    private BardSpiritVisualRenderer() {
    }

    public static void renderVisual(
            BardSpiritEntity entity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffers,
            int packedLight
    ) {
        if (entity == null || buffers == null || poseStack == null) return;

        byte key = entity.getVisualKey();
        if (key == NewwSpiritEntityBase.VISUAL_NONE) return;

        int life = entity.getLifetime();
        int start = entity.getVisualStartLifetime();
        int dur = Math.max(1, entity.getVisualDuration());

        float t = progress01(life, start, dur, partialTick);

        if (key == NewwSpiritEntityBase.VISUAL_HOVER) {
            float alpha = 0.65f;
            float radius = HOVER_RADIUS;

            float rot = (entity.tickCount + partialTick) * 6.0f;

            renderGroundQuad(entity, poseStack, buffers, packedLight, alpha, radius, rot);
            return;
        }

        if (key == NewwSpiritEntityBase.VISUAL_BURST) {
            float inv = (float) (1.0 + MathUtil.negate((double) t));
            if (inv <= 0.01f) return;

            float alpha = Math.min(1.0f, inv);
            float radius = BURST_RADIUS * (0.75f + (0.25f * t));

            float rot = (entity.tickCount + partialTick) * 12.0f;

            renderGroundQuad(entity, poseStack, buffers, packedLight, alpha, radius, rot);
        }
    }

    private static float progress01(int life, int start, int dur, float partialTick) {
        int deltaInt = life + (int) MathUtil.negate((double) start);
        float delta = deltaInt + partialTick;
        float v = delta / (float) dur;

        if (v < 0.0f) return 0.0f;
        if (v > 1.0f) return 1.0f;
        return v;
    }

    private static void renderGroundQuad(
            BardSpiritEntity entity,
            PoseStack poseStack,
            MultiBufferSource buffers,
            int packedLight,
            float alpha,
            float radius,
            float rotDegrees
    ) {
        if (entity.level() == null) return;

        int x = (int) Math.floor(entity.getX());
        int z = (int) Math.floor(entity.getZ());

        int top = entity.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        double groundY = (top + (int) MathUtil.negate(1.0)) + (double) GROUND_EPS;

        double dy = groundY + MathUtil.negate(entity.getY());

        poseStack.pushPose();

        poseStack.translate(0.0, dy, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotDegrees));

        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucent(VORTEX_TEX));

        PoseStack.Pose pose = poseStack.last();

        float y = 0.0f;

        float s = radius;
        float ns = (float) MathUtil.negate((double) s);

        int a = (int) (alpha * 255.0f);

        putVertex(vc, pose, ns, y, ns, 0.0f, 0.0f, packedLight, a);
        putVertex(vc, pose, s,  y, ns, 1.0f, 0.0f, packedLight, a);
        putVertex(vc, pose, s,  y, s,  1.0f, 1.0f, packedLight, a);

        putVertex(vc, pose, ns, y, ns, 0.0f, 0.0f, packedLight, a);
        putVertex(vc, pose, s,  y, s,  1.0f, 1.0f, packedLight, a);
        putVertex(vc, pose, ns, y, s,  0.0f, 1.0f, packedLight, a);

        poseStack.popPose();
    }

    private static void putVertex(
            VertexConsumer vc,
            PoseStack.Pose pose,
            float x,
            float y,
            float z,
            float u,
            float v,
            int packedLight,
            int alpha255
    ) {
        vc.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, alpha255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}