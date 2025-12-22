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

    private static final float ROTATION_SPEED = 0.2F;
    private static final float FLOW_SPEED = 0.5F;

    private static final int CONVERGE_TICKS = 5;
    private static final int LIFT_TICKS = 5;

    private static final float INNER_FACTOR = 0.33333334F;
    private static final float HALF_WIDTH = 0.05F;

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

    private final float baseRadius;
    private final float innerRadius;

    public MinorWindVortexVisual(
            Vec3 pos,
            long startGameTime,
            int loopDuration,
            int gapDuration,
            int loops,
            float sizeXZ
    ) {
        this.pos = pos;
        this.startGameTime = startGameTime;
        this.loopDuration = loopDuration;
        this.gapDuration = gapDuration;
        this.loops = Math.max(1, loops);

        int loopsMinusOne = this.loops + (~0);
        this.totalDuration = this.loops * loopDuration + Math.max(0, loopsMinusOne) * gapDuration;

        float s = Math.max(0.25F, sizeXZ);
        this.baseRadius = s * 0.5F;
        this.innerRadius = Math.max(0.05F, this.baseRadius * INNER_FACTOR);
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
        if (!state.active) return;
        if (state.alphaMul <= 0.0F) return;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        VertexConsumer vc = buffer.getBuffer(FragmentoRenderTypes.WIND_EMISSIVE);

        poseStack.pushPose();
        poseStack.translate(
                sub(pos.x, cam.x),
                sub(pos.y, cam.y) + state.yOffset,
                sub(pos.z, cam.z)
        );

        renderRibbons(poseStack, vc, state.localTime, state);

        poseStack.popPose();
    }

    private static double sub(double a, double b) {
        return a + neg(b);
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ 0x8000000000000000L);
    }

    private static int subInt(int a, int b) {
        return a + (~b + 1);
    }

    private float getTime(Minecraft mc, float partialTick) {
        return (mc.level.getGameTime() + (~startGameTime + 1L)) + partialTick;
    }

    private AnimationState computeAnimationState(float globalTime) {
        float t = globalTime;

        for (int i = 0; i < loops; i++) {
            int startI = i * (loopDuration + gapDuration);
            int endI = startI + loopDuration;

            if (t >= (float) startI && t < (float) endI) {
                float local = (float) (t + neg((float) startI));

                ConvergeLift cl = computeConvergeLift(local);

                float yOffset = 0.0F;
                float alphaMul = 1.0F;

                if (cl.lifting) {
                    yOffset = cl.liftPhase * LIFT_HEIGHT;
                    alphaMul = (float) (1.0F + neg(cl.liftPhase));
                }

                return new AnimationState(true, local, cl.converging, cl.convergePhase, cl.lifting, cl.liftPhase, alphaMul, yOffset);
            }
        }

        return AnimationState.INACTIVE;
    }

    private ConvergeLift computeConvergeLift(float localTime) {
        int startConvergeI = subInt(loopDuration, LIFT_TICKS + CONVERGE_TICKS);
        int endConvergeI = subInt(loopDuration, LIFT_TICKS);
        boolean converging = localTime >= (float) startConvergeI && localTime < (float) endConvergeI;
        float convergePhase = 0.0F;
        if (converging) {
            float num = (float) (localTime + neg((float) startConvergeI));
            convergePhase = num / (float) CONVERGE_TICKS;
        }

        int startLiftI = subInt(loopDuration, LIFT_TICKS);
        boolean lifting = localTime >= (float) startLiftI && localTime < (float) loopDuration;
        float liftPhase = 0.0F;
        if (lifting) {
            float num = (float) (localTime + neg((float) startLiftI));
            liftPhase = num / (float) LIFT_TICKS;
        }

        return new ConvergeLift(converging, convergePhase, lifting, liftPhase);
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
        float stepAngle = Mth.TWO_PI / (float) STEPS_PER_TURN;

        for (int r = 0; r < RIBBONS; r++) {
            float ribbonOffset = r * (Mth.TWO_PI / (float) RIBBONS);
            float baseAngle = rotation + ribbonOffset + flow * 0.05F;

            float prevOutX = 0.0F;
            float prevOutZ = 0.0F;
            float prevInX = 0.0F;
            float prevInZ = 0.0F;
            float prevAlpha = 0.0F;
            boolean hasPrev = false;

            for (int k = 0; k <= totalSteps; k++) {
                float p = k / (float) totalSteps;

                float baseR = Mth.lerp(p, baseRadius, innerRadius);
                float radius = baseR;

                if (state.converging) {
                    float k2 = (float) ((1.0F + neg(p)) * state.convergePhase);
                    float t2 = Mth.clamp(k2, 0.0F, 1.0F);
                    radius = Mth.lerp(t2, baseR, innerRadius);
                }

                if (state.lifting) {
                    radius = Mth.lerp(state.liftPhase, innerRadius, baseRadius * EXPAND_FACTOR);
                }

                float halfW = HALF_WIDTH * (radius / Math.max(baseR, 0.0001F));
                float rIn = (float) Math.max(radius + neg(halfW), 0.02F);
                float rOut = radius + halfW;

                float angle = baseAngle + (float) k * stepAngle;

                float outX = Mth.cos(angle) * rOut;
                float outZ = Mth.sin(angle) * rOut;
                float inX = Mth.cos(angle) * rIn;
                float inZ = Mth.sin(angle) * rIn;

                float alpha = Mth.lerp(p, 0.35F, 0.10F) * state.alphaMul;
                float y = Y_BASE + (float) r * Y_RIBBON_GAP + (float) k * Y_STEP_GAP;

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
        int overlayV = (OverlayTexture.NO_OVERLAY >>> 16) & 0xFFFF;

        vc.addVertex(pose, pOutX, y, pOutZ).setColor(1.0F, 1.0F, 1.0F, pAlpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, pInX, y, pInZ).setColor(1.0F, 1.0F, 1.0F, pAlpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, inX, y, inZ).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
        vc.addVertex(pose, outX, y, outZ).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.0F).setUv1(overlayU, overlayV).setUv2(light, light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private record ConvergeLift(boolean converging, float convergePhase, boolean lifting, float liftPhase) {
    }

    private static final class AnimationState {
        private static final AnimationState INACTIVE = new AnimationState(false, 0.0F, false, 0.0F, false, 0.0F, 0.0F, 0.0F);

        private final boolean active;
        private final float localTime;
        private final boolean converging;
        private final float convergePhase;
        private final boolean lifting;
        private final float liftPhase;
        private final float alphaMul;
        private final float yOffset;

        private AnimationState(
                boolean active,
                float localTime,
                boolean converging,
                float convergePhase,
                boolean lifting,
                float liftPhase,
                float alphaMul,
                float yOffset
        ) {
            this.active = active;
            this.localTime = localTime;
            this.converging = converging;
            this.convergePhase = convergePhase;
            this.lifting = lifting;
            this.liftPhase = liftPhase;
            this.alphaMul = alphaMul;
            this.yOffset = yOffset;
        }
    }
}
