package com.pgalaxyp.fragmento.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class MinorWindVortexVisual {

    private static final int RIBBONS = 4;

    // Quantos trapézios por volta, menor = mais quinas
    private static final int STEPS_PER_TURN = 12;

    // Quantas voltas até o centro
    private static final int TURNS = 3;

    private static final float BASE_RADIUS = 1.6F;
    private static final float INNER_RADIUS = 0.4F;

    private static final float HALF_WIDTH = 0.025F;

    private static final float ROTATION_SPEED = 0.22F;
    private static final float FLOW_SPEED = 0.7F;

    private static final float DURATION_TICKS = 90.0F;

    private static final int EFFECT_TICK_1 = 40;
    private static final int EFFECT_TICK_2 = 85;

    private static final int CONVERGE_TICKS = 5;
    private static final int LIFT_TICKS = 2;

    private static final float LIFT_HEIGHT = 2.8F;
    private static final float EXPAND_FACTOR = 2.2F;

    // Correção de profundidade
    private static final float Y_BASE = 0.02F;
    private static final float Y_RIBBON_GAP = 0.002F;
    private static final float Y_STEP_GAP = 0.00002F;

    private static final int LIGHT_U = 0xF0;
    private static final int LIGHT_V = 0xF0;

    private static final ResourceLocation TEXTURE = MissingTextureAtlasSprite.getLocation();

    private final Vec3 pos;
    private final long startGameTime;

    public MinorWindVortexVisual(Vec3 pos, long startGameTime) {
        this.pos = pos;
        this.startGameTime = startGameTime;
    }

    public boolean isExpired(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return true;
        float age = (mc.level.getGameTime() - startGameTime) + partialTick;
        return age >= DURATION_TICKS;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() - startGameTime) + partialTick;

        float convergePhase = -1.0F;
        float liftPhase = -1.0F;

        if (time >= EFFECT_TICK_1 - CONVERGE_TICKS && time < EFFECT_TICK_1) {
            convergePhase = (time - (EFFECT_TICK_1 - CONVERGE_TICKS)) / CONVERGE_TICKS;
        } else if (time >= EFFECT_TICK_1 && time < EFFECT_TICK_1 + LIFT_TICKS) {
            liftPhase = (time - EFFECT_TICK_1) / LIFT_TICKS;
        } else if (time >= EFFECT_TICK_2 - CONVERGE_TICKS && time < EFFECT_TICK_2) {
            convergePhase = (time - (EFFECT_TICK_2 - CONVERGE_TICKS)) / CONVERGE_TICKS;
        } else if (time >= EFFECT_TICK_2 && time < EFFECT_TICK_2 + LIFT_TICKS) {
            liftPhase = (time - EFFECT_TICK_2) / LIFT_TICKS;
        }

        float yOffset = 0.0F;
        float alphaMul = 1.0F;

        if (liftPhase >= 0.0F) {
            yOffset = liftPhase * LIFT_HEIGHT;
            alphaMul = 1.0F - liftPhase;
        }

        if (alphaMul <= 0.0F) return;

        float rotation = time * ROTATION_SPEED;
        float flow = time * FLOW_SPEED;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        poseStack.pushPose();
        poseStack.translate(
                pos.x - cam.x,
                pos.y - cam.y + yOffset,
                pos.z - cam.z
        );
        PoseStack.Pose pose = poseStack.last();

        int totalSteps = STEPS_PER_TURN * TURNS;
        float stepAngle = Mth.TWO_PI / (float) STEPS_PER_TURN;

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
                    vc.addVertex(pose, prevOutX, y, prevOutZ)
                            .setColor(1.0F, 1.0F, 1.0F, prevAlpha)
                            .setUv(0.0F, 0.0F).setUv1(0, 0).setUv2(LIGHT_U, LIGHT_V)
                            .setNormal(pose, 0.0F, 1.0F, 0.0F);

                    vc.addVertex(pose, prevInX, y, prevInZ)
                            .setColor(1.0F, 1.0F, 1.0F, prevAlpha)
                            .setUv(0.0F, 0.0F).setUv1(0, 0).setUv2(LIGHT_U, LIGHT_V)
                            .setNormal(pose, 0.0F, 1.0F, 0.0F);

                    vc.addVertex(pose, inX, y, inZ)
                            .setColor(1.0F, 1.0F, 1.0F, alpha)
                            .setUv(0.0F, 0.0F).setUv1(0, 0).setUv2(LIGHT_U, LIGHT_V)
                            .setNormal(pose, 0.0F, 1.0F, 0.0F);

                    vc.addVertex(pose, outX, y, outZ)
                            .setColor(1.0F, 1.0F, 1.0F, alpha)
                            .setUv(0.0F, 0.0F).setUv1(0, 0).setUv2(LIGHT_U, LIGHT_V)
                            .setNormal(pose, 0.0F, 1.0F, 0.0F);
                }

                prevOutX = outX;
                prevOutZ = outZ;
                prevInX = inX;
                prevInZ = inZ;
                prevAlpha = alpha;
                hasPrev = true;
            }
        }

        poseStack.popPose();
    }
}