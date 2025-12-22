package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class DemoHaloModel implements CosmeticModel {
    private static final float PX = 0.0625F;
    private static final float NEG_ONE = Float.intBitsToFloat(0xBF800000);

    private static final float THIN = 0.25F;
    private static final float LONG = 3.0F;
    private static final float DIST = 6.0F * PX;

    private static final float NDIST = DIST * NEG_ONE;

    private static final Cube8Model CUBE = new Cube8Model();

    @Override
    public void render(PoseStack poseStack, VertexConsumer vc, int packedLight, int packedOverlay) {
        if (poseStack == null) return;
        if (vc == null) return;

        poseStack.pushPose();

        renderBarX(poseStack, vc, packedLight, packedOverlay, DIST);
        renderBarX(poseStack, vc, packedLight, packedOverlay, NDIST);
        renderBarZ(poseStack, vc, packedLight, packedOverlay, DIST);
        renderBarZ(poseStack, vc, packedLight, packedOverlay, NDIST);

        poseStack.popPose();
    }

    private static void renderBarX(PoseStack poseStack, VertexConsumer vc, int light, int overlay, float z) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, z);
        poseStack.scale(LONG, THIN, THIN);
        CUBE.render(poseStack, vc, light, overlay);
        poseStack.popPose();
    }

    private static void renderBarZ(PoseStack poseStack, VertexConsumer vc, int light, int overlay, float x) {
        poseStack.pushPose();
        poseStack.translate(x, 0.0F, 0.0F);
        poseStack.scale(THIN, THIN, LONG);
        CUBE.render(poseStack, vc, light, overlay);
        poseStack.popPose();
    }
}