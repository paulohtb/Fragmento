package com.pgalaxyp.fragmento.rpg_old.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.rpg_old.content.entity.SpeedZoneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class SpeedZoneRenderer extends EntityRenderer<SpeedZoneEntity> {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/light_blue_concrete.png");

    public SpeedZoneRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            SpeedZoneEntity e,
            float yaw,
            float partial,
            PoseStack ps,
            MultiBufferSource buf,
            int light
    ) {
        ps.pushPose();

        var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        double cx = cam.getPosition().x;
        double cy = cam.getPosition().y;
        double cz = cam.getPosition().z;

        ps.translate(-cx, -cy, -cz);
        ps.translate(e.getX(), e.getY() + 0.02, e.getZ());

        float t = (e.tickCount + partial) * 2.2f;
        ps.mulPose(Axis.YP.rotationDegrees(t));

        float r = 3.5f;
        float s0 = r * 0.92f;
        float s1 = r * 0.74f;

        RenderType rt = RenderType.entityTranslucent(TEX);
        VertexConsumer vc = buf.getBuffer(rt);

        drawSquare(ps, vc, s0, 0.28f);
        ps.mulPose(Axis.YP.rotationDegrees(90.0f));
        drawSquare(ps, vc, s1, 0.22f);

        ps.popPose();
    }

    private static void drawSquare(PoseStack ps, VertexConsumer vc, float half, float a) {
        Matrix4f m = ps.last().pose();

        float x0 = -half;
        float z0 = -half;

        vc.addVertex(m, x0, 0f, z0).setColor(1f, 1f, 1f, a).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, half, 0f, z0).setColor(1f, 1f, 1f, a).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, half, 0f, half).setColor(1f, 1f, 1f, a).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, x0, 0f, half).setColor(1f, 1f, 1f, a).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpeedZoneEntity e) {
        return TEX;
    }
}