package com.pgalaxyp.fragmento.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

    private static final int CONVERGE_TICKS = 5;
    private static final int LIFT_TICKS = 5;

    private static final float LIFT_HEIGHT = 2.5F;
    private static final float EXPAND_FACTOR = 2.0F;

    private static final float Y_BASE = 0.02F;
    private static final float Y_RIBBON_GAP = 0.002F;
    private static final float Y_STEP_GAP = 0.00002F;

    private final Vec3 pos;
    private final long startGameTime;

    private final int loopDuration;
    private final int gapDuration;
    private final int loops;
    private final int totalDuration;

    public MinorWindVortexVisual(Vec3 pos, long startGameTime, int loopDuration, int gapDuration, int loops) {
        this.pos = pos;
        this.startGameTime = startGameTime;
        this.loopDuration = loopDuration;
        this.gapDuration = gapDuration;
        this.loops = Math.max(1, loops);
        this.totalDuration = this.loops * loopDuration + Math.max(0, this.loops - 1) * gapDuration;
    }

    public boolean isExpired(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return true;
        return getTime(mc, partialTick) >= totalDuration;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = getTime(mc, partialTick);
        AnimationState state = computeAnimationState(time);
        if (state.alphaMul <= 0.0F) return;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        VertexConsumer vc = buffer.getBuffer(FragmentoRenderTypes.WIND_EMISSIVE);

        poseStack.pushPose();
        poseStack.translate(
                pos.x - cam.x,
                pos.y - cam.y + state.yOffset,
                pos.z - cam.z
        );

        renderRibbons(poseStack, vc, state.localTime, state);

        poseStack.popPose();
    }

    private float getTime(Minecraft mc, float partialTick) {
        return (mc.level.getGameTime() - startGameTime) + partialTick;
    }

    private AnimationState computeAnimationState(float globalTime) {
        float t = globalTime;

        for (int i = 0; i < loops; i++) {
            float start = i * (loopDuration + gapDuration);
            float end = start + loopDuration;

            if (t >= start && t < end) {
                float local = t - start;

                float convergePhase = computeConverge(local);
                float liftPhase = computeLift(local);

                float yOffset = 0.0F;
                float alphaMul = 1.0F;

                if (liftPhase >= 0.0F) {
                    yOffset = liftPhase * LIFT_HEIGHT;
                    alphaMul = 1.0F - liftPhase;
                }

                return new AnimationState(local, convergePhase, liftPhase, alphaMul, yOffset);
            }
        }

        return AnimationState.INACTIVE;
    }

    private float computeConverge(float localTime) {
        float start = loopDuration - LIFT_TICKS - CONVERGE_TICKS;
        float end = loopDuration - LIFT_TICKS;

        if (localTime >= start && localTime < end) {
            return (localTime - start) / CONVERGE_TICKS;
        }
        return -1.0F;
    }

    private float computeLift(float localTime) {
        float start = loopDuration - LIFT_TICKS;
        if (localTime >= start && localTime < loopDuration) {
            return (localTime - start) / LIFT_TICKS;
        }
        return -1.0F;
    }

    private void renderRibbons(
            PoseStack poseStack,
            VertexConsumer vc,
            float localTime,
            AnimationState state
    ) {
        PoseStack.Pose pose = poseStack.last();

        float rotation = localTime * ROTATION_SPEED;
        float flow = localTime * FLOW_SPEED;

        int totalSteps = STEPS_PER_TURN * TURNS;
        float stepAngle = Mth.TWO_PI / STEPS_PER_TURN;

        for (int r = 0; r < RIBBONS; r++) {
            float ribbonOffset = r * (Mth.TWO_PI / RIBBONS);
            float baseAngle = rotation + ribbonOffset + flow * 0.05F;

            float prevOutX = 0.0F;
            float prevOutZ = 0.0F;
            float prevInX = 0.0F;
            float prevInZ = 0.0F;
            float prevAlpha = 0.0F;
            boolean hasPrev = false;

            for (int k = 0; k <= totalSteps; k++) {
                float p = k / (float) totalSteps;

                float baseRadius = Mth.lerp(p, BASE_RADIUS, INNER_RADIUS);
                float radius = baseRadius;

                if (state.convergePhase >= 0.0F) {
                    float k2 = (1.0F - p) * state.convergePhase;
                    radius = Mth.lerp(Mth.clamp(k2, 0.0F, 1.0F), baseRadius, INNER_RADIUS);
                }

                if (state.liftPhase >= 0.0F) {
                    radius = Mth.lerp(state.liftPhase, INNER_RADIUS, BASE_RADIUS * EXPAND_FACTOR);
                }

                float halfW = HALF_WIDTH * (radius / Math.max(baseRadius, 0.0001F));
                float rIn = Math.max(radius - halfW, 0.02F);
                float rOut = radius + halfW;

                float angle = baseAngle + k * stepAngle;

                float outX = Mth.cos(angle) * rOut;
                float outZ = Mth.sin(angle) * rOut;
                float inX = Mth.cos(angle) * rIn;
                float inZ = Mth.sin(angle) * rIn;

                float alpha = Mth.lerp(p, 0.35F, 0.10F) * state.alphaMul;
                float y = Y_BASE + r * Y_RIBBON_GAP + k * Y_STEP_GAP;

                if (hasPrev) {
                    addQuad(
                            vc,
                            pose,
                            prevOutX,
                            prevOutZ,
                            prevInX,
                            prevInZ,
                            prevAlpha,
                            outX,
                            outZ,
                            inX,
                            inZ,
                            alpha,
                            y
                    );
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
            VertexConsumer vc,
            PoseStack.Pose pose,
            float pOutX,
            float pOutZ,
            float pInX,
            float pInZ,
            float pAlpha,
            float outX,
            float outZ,
            float inX,
            float inZ,
            float alpha,
            float y
    ) {
        int light = 0xF000F0;
        int overlayU = OverlayTexture.NO_OVERLAY & 0xFFFF;
        int overlayV = (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF;

        vc.addVertex(pose, pOutX, y, pOutZ).setColor(1.0F, 1.0F, 1.0F, pAlpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, pInX, y, pInZ).setColor(1.0F, 1.0F, 1.0F, pAlpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, inX, y, inZ).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, outX, y, outZ).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private static final class AnimationState {
        private static final AnimationState INACTIVE = new AnimationState(0.0F, -1.0F, -1.0F, 0.0F, 0.0F);

        private final float localTime;
        private final float convergePhase;
        private final float liftPhase;
        private final float alphaMul;
        private final float yOffset;

        private AnimationState(float localTime, float convergePhase, float liftPhase, float alphaMul, float yOffset) {
            this.localTime = localTime;
            this.convergePhase = convergePhase;
            this.liftPhase = liftPhase;
            this.alphaMul = alphaMul;
            this.yOffset = yOffset;
        }
    }
}