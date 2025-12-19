package com.pgalaxyp.fragmento.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class AirImpactVisual {

    private static final int DURATION_TICKS = 8;
    private static final int SEGMENTS = 32;

    private final Vec3 pos;
    private final long startGameTime;

    public AirImpactVisual(Vec3 pos, long startGameTime) {
        this.pos = pos;
        this.startGameTime = startGameTime;
    }

    public boolean isExpired(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return true;
        float age = (float) (mc.level.getGameTime() - startGameTime) + partialTick;
        return age >= DURATION_TICKS;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float age = (float) (mc.level.getGameTime() - startGameTime) + partialTick;
        float progress = Mth.clamp(age / DURATION_TICKS, 0.0F, 1.0F);

        float radius = Mth.lerp(easeOut(progress), 0.2F, 3.5F);
        float alpha = Mth.lerp(progress, 0.28F, 0.0F);

        VertexConsumer vc = buffer.getBuffer(RenderType.lines());
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(
                pos.x - cam.x,
                pos.y - cam.y + 0.02,
                pos.z - cam.z
        );

        PoseStack.Pose pose = poseStack.last();

        for (int i = 0; i < SEGMENTS; i++) {
            float a1 = (float) i / SEGMENTS * Mth.TWO_PI;
            float a2 = (float) (i + 1) / SEGMENTS * Mth.TWO_PI;

            float noise1 = pseudoNoise(i) * 0.08F;
            float noise2 = pseudoNoise(i + 1) * 0.08F;

            float r1 = radius + noise1;
            float r2 = radius + noise2;

            float x1 = Mth.cos(a1) * r1;
            float z1 = Mth.sin(a1) * r1;
            float x2 = Mth.cos(a2) * r2;
            float z2 = Mth.sin(a2) * r2;

            vc.addVertex(pose, x1, 0.0F, z1)
                    .setColor(1.0F, 1.0F, 1.0F, alpha)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            vc.addVertex(pose, x2, 0.0F, z2)
                    .setColor(1.0F, 1.0F, 1.0F, alpha)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);
        }

        poseStack.popPose();
    }

    private static float easeOut(float t) {
        return 1.0F - (1.0F - t) * (1.0F - t);
    }

    private static float pseudoNoise(int i) {
        int n = i * 734287;
        n ^= (n << 13);
        return ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0F - 1.0F;
    }
}