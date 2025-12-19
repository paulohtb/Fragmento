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

    private static final int SEGMENTS = 40;
    private static final int RIBBONS = 4;

    private static final float HEIGHT = 3.0F;
    private static final float BASE_RADIUS = 1.6F;
    private static final float INNER_RADIUS = 0.2F;
    private static final float HALF_WIDTH = 0.12F;

    private static final float ROTATION_SPEED = 0.22F;
    private static final float FLOW_SPEED = 0.7F;

    private static final float DURATION_TICKS = 90.0F;

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
        float age = (float) (mc.level.getGameTime() - startGameTime) + partialTick;
        return age >= DURATION_TICKS;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() - startGameTime) + partialTick;
        float rotation = time * ROTATION_SPEED;
        float flow = time * FLOW_SPEED;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));

        poseStack.pushPose();
        poseStack.translate(
                pos.x - cam.x,
                pos.y - cam.y,
                pos.z - cam.z
        );

        PoseStack.Pose pose = poseStack.last();

        for (int r = 0; r < RIBBONS; r++) {
            float ribbonOffset = r * (Mth.TWO_PI / RIBBONS);

            for (int i = 0; i < SEGMENTS; i++) {
                if ((i & 1) == 0) continue;

                float t1 = (float) i / SEGMENTS;
                float t2 = (float) (i + 1) / SEGMENTS;

                float y1 = t1 * HEIGHT;
                float y2 = t2 * HEIGHT;

                float radius1 = Mth.lerp(t1, BASE_RADIUS, INNER_RADIUS);
                float radius2 = Mth.lerp(t2, BASE_RADIUS, INNER_RADIUS);

                float angle1 = rotation + ribbonOffset + t1 * Mth.TWO_PI + flow * 0.05F;
                float angle2 = rotation + ribbonOffset + t2 * Mth.TWO_PI + flow * 0.05F;

                float cx1 = Mth.cos(angle1) * radius1;
                float cz1 = Mth.sin(angle1) * radius1;
                float cx2 = Mth.cos(angle2) * radius2;
                float cz2 = Mth.sin(angle2) * radius2;

                float tx1 = Mth.cos(angle1 + Mth.HALF_PI);
                float tz1 = Mth.sin(angle1 + Mth.HALF_PI);
                float tx2 = Mth.cos(angle2 + Mth.HALF_PI);
                float tz2 = Mth.sin(angle2 + Mth.HALF_PI);

                float x1a = cx1 + tx1 * HALF_WIDTH;
                float z1a = cz1 + tz1 * HALF_WIDTH;
                float x1b = cx1 - tx1 * HALF_WIDTH;
                float z1b = cz1 - tz1 * HALF_WIDTH;

                float x2a = cx2 + tx2 * HALF_WIDTH;
                float z2a = cz2 + tz2 * HALF_WIDTH;
                float x2b = cx2 - tx2 * HALF_WIDTH;
                float z2b = cz2 - tz2 * HALF_WIDTH;

                float alpha1 = Mth.lerp(t1, 0.35F, 0.05F);
                float alpha2 = Mth.lerp(t2, 0.35F, 0.05F);

                vc.addVertex(pose, x1a, y1, z1a)
                        .setColor(1.0F, 1.0F, 1.0F, alpha1)
                        .setUv(0.0F, 0.0F)
                        .setUv1(0, 0)
                        .setUv2(LIGHT_U, LIGHT_V)
                        .setNormal(pose, 0.0F, 1.0F, 0.0F);

                vc.addVertex(pose, x1b, y1, z1b)
                        .setColor(1.0F, 1.0F, 1.0F, alpha1)
                        .setUv(0.0F, 0.0F)
                        .setUv1(0, 0)
                        .setUv2(LIGHT_U, LIGHT_V)
                        .setNormal(pose, 0.0F, 1.0F, 0.0F);

                vc.addVertex(pose, x2b, y2, z2b)
                        .setColor(1.0F, 1.0F, 1.0F, alpha2)
                        .setUv(0.0F, 0.0F)
                        .setUv1(0, 0)
                        .setUv2(LIGHT_U, LIGHT_V)
                        .setNormal(pose, 0.0F, 1.0F, 0.0F);

                vc.addVertex(pose, x2a, y2, z2a)
                        .setColor(1.0F, 1.0F, 1.0F, alpha2)
                        .setUv(0.0F, 0.0F)
                        .setUv1(0, 0)
                        .setUv2(LIGHT_U, LIGHT_V)
                        .setNormal(pose, 0.0F, 1.0F, 0.0F);
            }
        }

        poseStack.popPose();
    }
}