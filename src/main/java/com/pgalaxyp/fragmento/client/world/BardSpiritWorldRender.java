package com.pgalaxyp.fragmento.client.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class BardSpiritWorldRender {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/wind_vortex.png");

    private static final float HOVER_RADIUS = 2.5f;
    private static final float GROUND_EPS = 0.02f;

    private BardSpiritWorldRender() {
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.cameraEntity == null) return;

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

        double camX = mc.gameRenderer.getMainCamera().getPosition().x;
        double camY = mc.gameRenderer.getMainCamera().getPosition().y;
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z;

        for (BardSpiritEntity spirit : mc.level.getEntitiesOfClass(
                BardSpiritEntity.class,
                mc.cameraEntity.getBoundingBox().inflate(64)
        )) {

            byte key = spirit.getVisualKey();
            if (key != NewwSpiritEntityBase.VISUAL_HOVER
                    && key != NewwSpiritEntityBase.VISUAL_BURST) continue;

            int x = Mth.floor(spirit.getX());
            int z = Mth.floor(spirit.getZ());

            int top = mc.level.getHeight(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    x,
                    z
            );

            double groundY = top - 1 + GROUND_EPS;
            float rot = (spirit.tickCount + partialTick) * 6.0f;

            poseStack.pushPose();
            poseStack.translate(
                    spirit.getX() - camX,
                    groundY - camY,
                    spirit.getZ() - camZ
            );
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));

            VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucent(TEX));
            PoseStack.Pose pose = poseStack.last();

            float r = HOVER_RADIUS;
            float n = -r;
            int light = 0xF000F0;
            int alpha = key == NewwSpiritEntityBase.VISUAL_BURST ? 200 : 160;

            vc.addVertex(pose, n, 0, n).setColor(255, 255, 255, alpha).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
            vc.addVertex(pose, r, 0, n).setColor(255, 255, 255, alpha).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
            vc.addVertex(pose, r, 0, r).setColor(255, 255, 255, alpha).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);

            vc.addVertex(pose, n, 0, n).setColor(255, 255, 255, alpha).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
            vc.addVertex(pose, r, 0, r).setColor(255, 255, 255, alpha).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
            vc.addVertex(pose, n, 0, r).setColor(255, 255, 255, alpha).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);

            poseStack.popPose();
        }

        buffers.endBatch();
    }
}