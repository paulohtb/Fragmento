package com.pgalaxyp.fragmento.rpg.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.rpg.content.entity.InfusedStrikeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class InfusedStrikeRenderer extends EntityRenderer<InfusedStrikeEntity> {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/cut.png");

    public InfusedStrikeRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            InfusedStrikeEntity e,
            float yaw,
            float partial,
            PoseStack ps,
            MultiBufferSource buf,
            int light
    ) {
        ps.pushPose();

        if (e.isImpactFlash()) {
            renderImpact(e, partial, ps, buf);
        }

        if (e.isVortexActive()) {
            renderVortex(e, partial, ps, buf);
        } else {
            renderDescend(e, partial, ps, buf);
        }

        ps.popPose();
    }

    private void renderDescend(InfusedStrikeEntity e, float partial, PoseStack ps, MultiBufferSource buf) {
        var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf q = cam.rotation();

        ps.translate(0.0, 0.6, 0.0);
        ps.mulPose(q);

        float t = (e.ageTicks() + partial) * 0.22f;
        ps.mulPose(Axis.ZP.rotation(t));

        VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucent(TEX));

        float s = 0.85f;
        float w = 0.22f;
        float h = 2.15f;

        Matrix4f m = ps.last().pose();

        quad(vc, m, -w, -h, 0f, w, h, 0f, 0f, 1f, 1f, 0f, 0.65f);
        quad(vc, m, -s, -0.18f, -0.02f, s, 0.18f, -0.02f, 0f, 1f, 1f, 0f, 0.25f);
    }

    private void renderImpact(InfusedStrikeEntity e, float partial, PoseStack ps, MultiBufferSource buf) {
        float t = (e.phaseTicks() + partial) / 3.0f;
        t = Math.min(1f, Math.max(0f, t));

        VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucent(TEX));

        ps.pushPose();
        ps.translate(0.0, 0.02, 0.0);
        ps.mulPose(Axis.XP.rotation(1.5707964f));

        float r0 = 0.25f + 1.15f * t;
        float w = 0.12f * (1f - 0.75f * t);

        Matrix4f m = ps.last().pose();

        for (int i = 0; i < 6; i++) {
            ps.pushPose();
            ps.mulPose(Axis.ZP.rotation((float) (i * (Math.PI / 6.0))));
            Matrix4f mi = ps.last().pose();
            quad(vc, mi, -r0, -w, 0f, r0, w, 0f, 0f, 1f, 1f, 0f, 0.55f * (1f - t));
            ps.popPose();
        }

        ps.popPose();
    }

    private void renderVortex(InfusedStrikeEntity e, float partial, PoseStack ps, MultiBufferSource buf) {
        float time = (e.ageTicks() + partial) * 0.18f;
        float pull = Math.min(1f, (e.phaseTicks() + partial) / 14.0f);

        VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucent(TEX));

        ps.pushPose();
        ps.translate(0.0, 0.05, 0.0);

        for (int i = 0; i < 10; i++) {
            ps.pushPose();

            float layer = i / 9.0f;
            float y = 0.12f + layer * 0.85f;
            float spin = time * (1.1f + 0.7f * layer) * (i % 2 == 0 ? 1f : -1f);

            ps.translate(0.0, y, 0.0);
            ps.mulPose(Axis.YP.rotation(spin));
            ps.mulPose(Axis.XP.rotation(1.15f + 0.25f * layer));

            float r = 1.05f + 1.55f * pull - layer * 0.35f;
            float w = 0.10f + 0.10f * (1f - layer);

            Matrix4f m = ps.last().pose();

            float a = 0.10f + 0.28f * (1f - layer);
            quad(vc, m, -r, -w, 0f, r, w, 0f, 0f, 1f, 1f, 0f, a);

            ps.popPose();
        }

        var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf q = cam.rotation();
        Vector3f camRight = new Vector3f(1f, 0f, 0f).rotate(q);
        Vector3f camUp = new Vector3f(0f, 1f, 0f).rotate(q);

        ps.pushPose();
        ps.translate(0.0, 0.55, 0.0);
        ps.mulPose(q);

        float wobble = (float) Math.sin(time * 1.7f) * 0.35f * pull;
        ps.mulPose(Axis.ZP.rotation(wobble));

        Matrix4f m2 = ps.last().pose();
        float s = 1.35f + 0.55f * pull;
        quad(vc, m2, -s, -s, 0f, s, s, 0f, 0f, 1f, 1f, 0f, 0.16f);

        ps.popPose();
        ps.popPose();
    }

    private static void quad(
            VertexConsumer vc,
            Matrix4f m,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1,
            float u0,
            float v0,
            float u1,
            float v1,
            float a
    ) {
        vc.addVertex(m, x0, y0, z0).setColor(1f, 1f, 1f, a).setUv(u0, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, x1, y0, z1).setColor(1f, 1f, 1f, a).setUv(u1, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, x1, y1, z1).setColor(1f, 1f, 1f, a).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
        vc.addVertex(m, x0, y1, z0).setColor(1f, 1f, 1f, a).setUv(u0, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 1f, 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(InfusedStrikeEntity e) {
        return TEX;
    }
}