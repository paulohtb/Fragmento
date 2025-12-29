package com.pgalaxyp.fragmento.cosmetics.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticModel;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticModels;
import com.pgalaxyp.fragmento.cosmetics.client.render.resources.CosmeticRenderResources;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientRegistries;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final double MAX_DISTANCE_SQ = 64.0 * 64.0;
    private static final ConcurrentHashMap<String, ResourceLocation> RL_CACHE = new ConcurrentHashMap<>();
    private static final CosmeticSlot[] SLOTS = CosmeticSlot.values();

    private final CosmeticRegistry registry;

    public CosmeticsPlayerLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        this.registry = CosmeticsClientRegistries.registry();
    }

    public CosmeticsPlayerLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            CosmeticRegistry registry
    ) {
        super(parent);
        this.registry = registry;
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffers,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (poseStack == null || buffers == null || player == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) {
            return;
        }

        UUID self = mc.player.getUUID();
        UUID id = player.getUUID();

        if (!self.equals(id)) {
            if (!CosmeticsClientState.showOthers()) {
                return;
            }

            double dx = sub(player.getX(), mc.player.getX());
            double dy = sub(player.getY(), mc.player.getY());
            double dz = sub(player.getZ(), mc.player.getZ());
            if (dx * dx + dy * dy + dz * dz > MAX_DISTANCE_SQ) {
                return;
            }
        }

        CosmeticLoadout loadout = CosmeticsClientState.getEffective(id);
        if (loadout == null || loadout.isEmpty()) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = getParentModel();
        if (model == null) {
            return;
        }

        for (CosmeticSlot slot : SLOTS) {
            CosmeticId cosmeticId = loadout.get(slot);
            if (cosmeticId == null) {
                continue;
            }

            ResourceLocation rl = parseCached(cosmeticId.value());
            if (rl == null) {
                continue;
            }

            CosmeticDefinition def = registry.get(cosmeticId);
            if (def == null) {
                continue;
            }

            if (self.equals(id) && !def.visibleToSelf()) {
                continue;
            }

            CosmeticModel cosmeticModel = CosmeticModels.get(rl);
            ResourceLocation texture = CosmeticRenderResources.textureFor(rl);
            RenderType rt = CosmeticRenderResources.cutoutNoCull(texture);
            if (rt == null) {
                continue;
            }

            VertexConsumer vc = buffers.getBuffer(rt);
            renderSlot(poseStack, vc, packedLight, slot, cosmeticModel, model);
        }
    }

    private static double sub(double a, double b) {
        return a + neg(b);
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ 0x8000000000000000L);
    }

    private static ResourceLocation parseCached(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ResourceLocation cached = RL_CACHE.get(value);
        if (cached != null) {
            return cached;
        }
        ResourceLocation parsed = ResourceLocation.tryParse(value);
        if (parsed == null) {
            return null;
        }
        ResourceLocation raced = RL_CACHE.putIfAbsent(value, parsed);
        return raced == null ? parsed : raced;
    }

    private void renderSlot(
            PoseStack poseStack,
            VertexConsumer vc,
            int packedLight,
            CosmeticSlot slot,
            CosmeticModel cosmeticModel,
            PlayerModel<AbstractClientPlayer> playerModel
    ) {
        if (slot == CosmeticSlot.HEAD) {
            renderOnPart(poseStack, vc, packedLight, cosmeticModel, playerModel.head, slot);
            return;
        }
        if (slot == CosmeticSlot.BODY || slot == CosmeticSlot.BACK || slot == CosmeticSlot.WAIST) {
            renderOnPart(poseStack, vc, packedLight, cosmeticModel, playerModel.body, slot);
            return;
        }
        if (slot == CosmeticSlot.HAND) {
            renderOnPart(poseStack, vc, packedLight, cosmeticModel, playerModel.rightArm, slot);
        }
    }

    private void renderOnPart(
            PoseStack poseStack,
            VertexConsumer vc,
            int packedLight,
            CosmeticModel cosmeticModel,
            ModelPart anchor,
            CosmeticSlot slot
    ) {
        if (anchor == null) {
            return;
        }

        poseStack.pushPose();
        anchor.translateAndRotate(poseStack);

        applySlotOffset(poseStack, slot);

        cosmeticModel.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private static void applySlotOffset(PoseStack poseStack, CosmeticSlot slot) {
        if (slot == CosmeticSlot.HEAD) {
            poseStack.translate(0.0F, PX, NEG_ONE * PX * 4.0F);
            return;
        }
        if (slot == CosmeticSlot.BODY) {
            poseStack.translate(0.0F, PX * 2.0F, PX * 2.0F);
            return;
        }
        if (slot == CosmeticSlot.BACK) {
            poseStack.translate(0.0F, PX * 2.0F, NEG_ONE * PX * 3.0F);
            return;
        }
        if (slot == CosmeticSlot.WAIST) {
            poseStack.translate(0.0F, NEG_ONE * PX, PX * 2.0F);
            return;
        }
        if (slot == CosmeticSlot.HAND) {
            poseStack.translate(PX, PX, 0.0F);
        }
    }
}