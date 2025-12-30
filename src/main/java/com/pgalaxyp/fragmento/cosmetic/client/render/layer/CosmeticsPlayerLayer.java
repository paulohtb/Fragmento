package com.pgalaxyp.fragmento.cosmetic.client.render.layer;

import com.pgalaxyp.fragmento.cosmetic.client.render.model.CosmeticModel;
import com.pgalaxyp.fragmento.cosmetic.client.render.model.CosmeticModels;
import com.pgalaxyp.fragmento.cosmetic.client.render.resources.CosmeticRenderResources;
import com.pgalaxyp.fragmento.cosmetic.client.state.ClientCosmetics;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Objects;
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
    private static final double MAX_DISTANCE_SQ = 4096.0;
    private static final ConcurrentHashMap<String, ResourceLocation> RL_CACHE = new ConcurrentHashMap<>();

    public CosmeticsPlayerLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
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
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return;

        UUID self = mc.player.getUUID();
        UUID id = player.getUUID();

        if (!self.equals(id)) {
            if (!ClientCosmetics.showOthers()) return;

            double dx = player.getX() - mc.player.getX();
            double dy = player.getY() - mc.player.getY();
            double dz = player.getZ() - mc.player.getZ();
            if (dx * dx + dy * dy + dz * dz > MAX_DISTANCE_SQ) return;
        }

        PlayerModel<AbstractClientPlayer> model = getParentModel();

        for (CosmeticSlot slot : CosmeticSlot.values()) {
            CosmeticId equipped = ClientCosmetics.equipped(id, slot);
            if (equipped == null) continue;

            List<CosmeticEntry> entries = ClientCosmetics.entriesBySlot(id, slot);
            CosmeticEntry entry = null;
            for (CosmeticEntry e : entries) {
                if (e != null && Objects.equals(e.id(), equipped)) {
                    entry = e;
                    break;
                }
            }
            if (entry == null || entry.info() == null) continue;

            if (self.equals(id) && !entry.info().visibleToSelf()) continue;

            ResourceLocation rl = RL_CACHE.computeIfAbsent(entry.info().modelKey(), ResourceLocation::tryParse);
            if (rl == null) continue;

            CosmeticModel m = CosmeticModels.get(rl);
            RenderType rt = CosmeticRenderResources.cutoutNoCull(CosmeticRenderResources.textureFor(rl));
            if (rt == null) continue;

            renderSlot(poseStack, buffers.getBuffer(rt), packedLight, slot, m, model);
        }
    }

    private void renderSlot(
            PoseStack poseStack,
            VertexConsumer vc,
            int light,
            CosmeticSlot slot,
            CosmeticModel model,
            PlayerModel<AbstractClientPlayer> pm
    ) {
        ModelPart part =
                slot == CosmeticSlot.HEAD ? pm.head :
                        slot == CosmeticSlot.HAND ? pm.rightArm :
                                pm.body;

        poseStack.pushPose();
        part.translateAndRotate(poseStack);

        if (slot == CosmeticSlot.HEAD) poseStack.translate(0, PX, -PX * 4);
        if (slot == CosmeticSlot.BACK) poseStack.translate(0, PX * 2, -PX * 3);
        if (slot == CosmeticSlot.WAIST) poseStack.translate(0, -PX, PX * 2);

        model.render(poseStack, vc, light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}