package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.PoseStack;

public interface CosmeticModel {

    void render(PoseStack poseStack, VertexConsumer vc, int packedLight, int packedOverlay);

    default void renderDefault(PoseStack poseStack, VertexConsumer vc, int packedLight) {
        render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
    }
}