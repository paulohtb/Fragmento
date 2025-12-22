package com.pgalaxyp.fragmento.cosmetics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTransform;
import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticModel;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticModels;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticsRuntime;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public final class CosmeticsPlayerLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final float PX = 0.0625F;
    private static final float NEG_ONE = Float.intBitsToFloat(0xBF800000);
    private static final float HEAD_TOP_MODEL = 8.0F * PX * NEG_ONE;
    private static final float LEG_BOTTOM_MODEL = 12.0F * PX;

    public CosmeticsPlayerLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (poseStack == null) return;
        if (buffers == null) return;
        if (player == null) return;

        Minecraft mc = Minecraft.getInstance();
        UUID self = mc != null && mc.player != null ? mc.player.getUUID() : null;
        UUID id = player.getUUID();

        if (self != null && self.equals(id)) {
            if (mc != null && mc.options != null && mc.options.getCameraType().isFirstPerson()) {
                return;
            }
        } else {
            if (!CosmeticsClientState.showOthers()) {
                return;
            }
        }

        CosmeticLoadout loadout = CosmeticsClientState.getEffective(id);
        if (loadout == null || loadout.isEmpty()) return;

        PlayerModel<AbstractClientPlayer> model = getParentModel();
        if (model == null) return;

        for (Map.Entry<CosmeticSlot, CosmeticId> e : loadout.equippedView().entrySet()) {
            CosmeticSlot slot = e.getKey();
            CosmeticId cosmeticId = e.getValue();
            if (slot == null || cosmeticId == null) continue;

            ResourceLocation rl = cosmeticId.value();
            if (rl == null) continue;

            CosmeticDefinition def = CosmeticsRuntime.registry().getDefinition(cosmeticId);
            if (def == null) continue;

            if (self != null && self.equals(id) && !def.visibleToSelf()) {
                continue;
            }

            CosmeticModel cosmeticModel = CosmeticModels.get(rl);
            ResourceLocation texture = CosmeticRenderResources.textureFor(rl);
            RenderType rt = CosmeticRenderResources.cutoutNoCull(texture);
            if (rt == null) continue;
            VertexConsumer vc = buffers.getBuffer(rt);

            renderSlot(poseStack, vc, packedLight, slot, def, cosmeticModel, model);
        }
    }

    private void renderSlot(PoseStack poseStack, VertexConsumer vc, int packedLight, CosmeticSlot slot, CosmeticDefinition def, CosmeticModel cosmeticModel, PlayerModel<AbstractClientPlayer> playerModel) {
        if (slot == CosmeticSlot.HEAD) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.head, 1);
            return;
        }
        if (slot == CosmeticSlot.BODY) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.body, 0);
            return;
        }
        if (slot == CosmeticSlot.ARMS) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.leftArm, 0);
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.rightArm, 0);
            return;
        }
        if (slot == CosmeticSlot.LEGS) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.leftLeg, 0);
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.rightLeg, 0);
            return;
        }
        if (slot == CosmeticSlot.FEET) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.leftLeg, 2);
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.rightLeg, 2);
            return;
        }
        if (slot == CosmeticSlot.CAPE) {
            renderOnPart(poseStack, vc, packedLight, slot, def, cosmeticModel, playerModel.body, 0);
        }
    }

    private void renderOnPart(PoseStack poseStack, VertexConsumer vc, int packedLight, CosmeticSlot slot, CosmeticDefinition def, CosmeticModel cosmeticModel, ModelPart anchor, int anchorKind) {
        if (anchor == null) return;

        poseStack.pushPose();
        anchor.translateAndRotate(poseStack);

        if (anchorKind == 1) {
            poseStack.translate(0.0F, HEAD_TOP_MODEL, 0.0F);
        } else if (anchorKind == 2) {
            poseStack.translate(0.0F, LEG_BOTTOM_MODEL, 0.0F);
        }

        applyTransformPixels(poseStack, slot.defaultTransform());
        applyTransformPixels(poseStack, def.transform());

        cosmeticModel.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private static void applyTransformPixels(PoseStack poseStack, CosmeticTransform t) {
        if (poseStack == null) return;
        if (t == null) return;

        float tx = t.offsetX() * PX;
        float ty = t.offsetY() * PX * NEG_ONE;
        float tz = t.offsetZ() * PX;

        poseStack.translate(tx, ty, tz);

        float rx = t.rotXDeg();
        float ry = t.rotYDeg();
        float rz = t.rotZDeg();

        if (rx != 0.0F) poseStack.mulPose(Axis.XP.rotationDegrees(rx));
        if (ry != 0.0F) poseStack.mulPose(Axis.YP.rotationDegrees(ry));
        if (rz != 0.0F) poseStack.mulPose(Axis.ZP.rotationDegrees(rz));

        float s = t.scale();
        if (s != 1.0F) poseStack.scale(s, s, s);
    }
}