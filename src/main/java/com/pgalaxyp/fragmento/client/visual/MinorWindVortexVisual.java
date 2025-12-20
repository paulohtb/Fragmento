package com.pgalaxyp.fragmento.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class MinorWindVortexVisual {

    private static final int RIBBONS = 4;
    private static final int STEPS_PER_TURN = 8;
    private static final int TURNS = 1;

    private static final float BASE_RADIUS = 1.5F;
    private static final float INNER_RADIUS = 0.5F;
    private static final float HALF_WIDTH = 0.05F;

    private static final float ROTATION_SPEED = 0.2F;
    private static final float FLOW_SPEED = 0.5F;

    private static final int PULSE_DURATION = 40;
    private static final int GAP_DURATION = 5;
    private static final int TOTAL_DURATION = 85;

    private static final int CONVERGE_TICKS = 5;
    private static final int LIFT_TICKS = 5;

    private static final float LIFT_HEIGHT = 2.5F;
    private static final float EXPAND_FACTOR = 2F;

    private static final float Y_BASE = 0.02F;
    private static final float Y_RIBBON_GAP = 0.002F;
    private static final float Y_STEP_GAP = 0.00002F;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/white.png");

    private final Vec3 pos;
    private final long startGameTime;

    public MinorWindVortexVisual(Vec3 pos, long startGameTime) {
        this.pos = pos;
        this.startGameTime = startGameTime;
    }

    public boolean isExpired(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return true;
        return getTime(mc, partialTick) >= TOTAL_DURATION;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = getTime(mc, partialTick);
        float pulseTime = getPulseTime(time);

        float convergePhase = -1.0F;
        float liftPhase = -1.0F;
        float alphaMul = 1.0F;
        float yOffset = 0.0F;

        if (pulseTime >= 0.0F) {
            convergePhase = computeConverge(pulseTime);
            liftPhase = computeLift(pulseTime);

            if (liftPhase >= 0.0F) {
                yOffset = liftPhase * LIFT_HEIGHT;
                alphaMul = 1.0F - liftPhase;
            }
        }

        if (alphaMul <= 0.0F) return;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        VertexConsumer vc = buffer.getBuffer(FragmentoRenderTypes.WIND_EMISSIVE);

        poseStack.pushPose();
        poseStack.translate(
                pos.x - cam.x,
                pos.y - cam.y + yOffset,
                pos.z - cam.z
        );

        renderRibbons(poseStack, vc, time, convergePhase, liftPhase, alphaMul);

        poseStack.popPose();
    }

    private float getTime(Minecraft mc, float partialTick) {
        return (mc.level.getGameTime() - startGameTime) + partialTick;
    }

    private float getPulseTime(float time) {
        if (time < PULSE_DURATION) {
            return time;
        }

        if (time < PULSE_DURATION + GAP_DURATION) {
            return -1.0F;
        }

        if (time < TOTAL_DURATION) {
            return time - (PULSE_DURATION + GAP_DURATION);
        }

        return -1.0F;
    }

    private float computeConverge(float pulseTime) {
        float convergeStart = PULSE_DURATION - LIFT_TICKS - CONVERGE_TICKS;
        float convergeEnd = PULSE_DURATION - LIFT_TICKS;

        if (pulseTime >= convergeStart && pulseTime < convergeEnd) {
            return (pulseTime - convergeStart) / CONVERGE_TICKS;
        }
        return -1.0F;
    }

    private float computeLift(float pulseTime) {
        float liftStart = PULSE_DURATION - LIFT_TICKS;

        if (pulseTime >= liftStart && pulseTime < PULSE_DURATION) {
            return (pulseTime - liftStart) / LIFT_TICKS;
        }
        return -1.0F;
    }

    private void renderRibbons(
            PoseStack poseStack,
            VertexConsumer vc,
            float time,
            float convergePhase,
            float liftPhase,
            float alphaMul
    ) {
        PoseStack.Pose pose = poseStack.last();

        float rotation = time * ROTATION_SPEED;
        float flow = time * FLOW_SPEED;

        int totalSteps = STEPS_PER_TURN * TURNS;
        float stepAngle = Mth.TWO_PI / STEPS_PER_TURN;

        for (int r = 0; r < RIBBONS; r++) {
            float ribbonOffset = r * (Mth.TWO_PI / RIBBONS);
            float baseAngle = rotation + ribbonOffset + flow * 0.05F;

            float prevOutX = 0, prevOutZ = 0, prevInX = 0, prevInZ = 0, prevAlpha = 0;
            boolean hasPrev = false;

            for (int k = 0; k <= totalSteps; k++) {
                float p = k / (float) totalSteps;

                float baseRadius = Mth.lerp(p, BASE_RADIUS, INNER_RADIUS);
                float radius = baseRadius;

                if (convergePhase >= 0.0F) {
                    float k2 = (1.0F - p) * convergePhase;
                    radius = Mth.lerp(Mth.clamp(k2, 0.0F, 1.0F), baseRadius, INNER_RADIUS);
                }

                if (liftPhase >= 0.0F) {
                    radius = Mth.lerp(liftPhase, INNER_RADIUS, BASE_RADIUS * EXPAND_FACTOR);
                }

                float halfW = HALF_WIDTH * (radius / Math.max(baseRadius, 0.0001F));
                float rIn = Math.max(radius - halfW, 0.02F);
                float rOut = radius + halfW;

                float angle = baseAngle + k * stepAngle;

                float outX = Mth.cos(angle) * rOut;
                float outZ = Mth.sin(angle) * rOut;
                float inX = Mth.cos(angle) * rIn;
                float inZ = Mth.sin(angle) * rIn;

                float alpha = Mth.lerp(p, 0.35F, 0.10F) * alphaMul;
                float y = Y_BASE + r * Y_RIBBON_GAP + k * Y_STEP_GAP;

                if (hasPrev) {
                    addQuad(vc, pose, prevOutX, prevOutZ, prevInX, prevInZ, prevAlpha, outX, outZ, inX, inZ, alpha, y);
                }

                prevOutX = outX;
                prevOutZ = outZ;
                prevInX = inX;
                prevInZ = inZ;
                prevAlpha = alpha;
                hasPrev = true;
            }
        }
    }

    private void addQuad(
            VertexConsumer vc, PoseStack.Pose pose,
            float pOutX, float pOutZ, float pInX, float pInZ,
            float pAlpha, float outX, float outZ, float inX, float inZ,
            float alpha, float y
    ) {
        int light = 0xF000F0;
        int overlayU = OverlayTexture.NO_OVERLAY & 0xFFFF;
        int overlayV = (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF;

        vc.addVertex(pose, pOutX, y, pOutZ)
                .setColor(1.0F, 1.0F, 1.0F, pAlpha)
                .setUv(0.0F, 0.0F)
                .setUv1(overlayU, overlayV)
                .setUv2(light, light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        vc.addVertex(pose, pInX, y, pInZ)
                .setColor(1.0F, 1.0F, 1.0F, pAlpha)
                .setUv(0.0F, 0.0F)
                .setUv1(overlayU, overlayV)
                .setUv2(light, light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        vc.addVertex(pose, inX, y, inZ)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv(0.0F, 0.0F)
                .setUv1(overlayU, overlayV)
                .setUv2(light, light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        vc.addVertex(pose, outX, y, outZ)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv(0.0F, 0.0F)
                .setUv1(overlayU, overlayV)
                .setUv2(light, light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}