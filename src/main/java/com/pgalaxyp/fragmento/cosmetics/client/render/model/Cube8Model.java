package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class Cube8Model implements CosmeticModel {

    private static final float PX = 0.0625F;
    private static final float NEG_ONE = Float.intBitsToFloat(0xBF800000);

    @Override
    public void render(PoseStack poseStack, VertexConsumer vc, int packedLight, int packedOverlay) {
        if (poseStack == null) return;
        if (vc == null) return;
        renderCube(poseStack, vc, packedLight, packedOverlay);
    }

    private static void renderCube(PoseStack poseStack, VertexConsumer vc, int light, int overlay) {
        PoseStack.Pose last = poseStack.last();

        float half = 2.0F * PX;
        float nHalf = half * NEG_ONE;

        quad(vc, last, light, overlay, half, half, half, half, half, nHalf, half, nHalf, nHalf, half, nHalf, half, 1.0F, 0.0F, 0.0F);
        quad(vc, last, light, overlay, nHalf, half, nHalf, nHalf, half, half, nHalf, nHalf, half, nHalf, nHalf, nHalf, NEG_ONE, 0.0F, 0.0F);
        quad(vc, last, light, overlay, nHalf, half, half, half, half, half, half, nHalf, half, nHalf, nHalf, half, 0.0F, 0.0F, 1.0F);
        quad(vc, last, light, overlay, half, half, nHalf, nHalf, half, nHalf, nHalf, nHalf, nHalf, half, nHalf, nHalf, 0.0F, 0.0F, NEG_ONE);
        quad(vc, last, light, overlay, nHalf, half, nHalf, half, half, nHalf, half, half, half, nHalf, half, half, 0.0F, 1.0F, 0.0F);
        quad(vc, last, light, overlay, nHalf, nHalf, half, half, nHalf, half, half, nHalf, nHalf, nHalf, nHalf, nHalf, 0.0F, NEG_ONE, 0.0F);
    }

    private static void quad(
            VertexConsumer vc,
            PoseStack.Pose pose,
            int light,
            int overlay,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            float nx, float ny, float nz
    ) {
        v(vc, pose, light, overlay, x1, y1, z1, 0.0F, 0.0F, nx, ny, nz);
        v(vc, pose, light, overlay, x2, y2, z2, 1.0F, 0.0F, nx, ny, nz);
        v(vc, pose, light, overlay, x3, y3, z3, 1.0F, 1.0F, nx, ny, nz);
        v(vc, pose, light, overlay, x4, y4, z4, 0.0F, 1.0F, nx, ny, nz);
    }

    private static void v(
            VertexConsumer vc,
            PoseStack.Pose pose,
            int light,
            int overlay,
            float x,
            float y,
            float z,
            float u,
            float vv,
            float nx,
            float ny,
            float nz
    ) {
        vc.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, vv)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}