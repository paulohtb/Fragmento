package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface CosmeticModel {
    void render(PoseStack poseStack, VertexConsumer vc, int packedLight, int packedOverlay);
}