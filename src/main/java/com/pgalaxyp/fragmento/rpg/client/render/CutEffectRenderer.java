package com.pgalaxyp.fragmento.rpg.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class CutEffectRenderer extends EntityRenderer<CutEffectEntity> {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/cut.png");

    public CutEffectRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            CutEffectEntity e,
            float yaw,
            float partial,
            PoseStack ps,
            MultiBufferSource buf,
            int light
    ) {
        ps.pushPose();

        ps.translate(0.0, e.getBbHeight() * 0.5, 0.0);

        var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf q = cam.rotation();

        Vector3f camRight = new Vector3f(1f, 0f, 0f).rotate(q);
        Vector3f camUp = new Vector3f(0f, 1f, 0f).rotate(q);

        Vec3 dir = e.aimDir().normalize();

        float dx = (float) (dir.x * camRight.x + dir.y * camRight.y + dir.z * camRight.z);
        float dy = (float) (dir.x * camUp.x + dir.y * camUp.y + dir.z * camUp.z);

        if (dx * dx + dy * dy < 1.0e-8f) {
            dx = 1f;
            dy = 0f;
        }

        float ang = (float) Math.atan2(dy, dx);

        ps.mulPose(q);
        ps.mulPose(Axis.ZP.rotation(ang));

        float s = 0.6f;

        Matrix4f m = ps.last().pose();
        VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull(TEX));

        vc.addVertex(m, -s, -s, 0f).setColor(1f, 1f, 1f, 1f).setUv(0f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 0f, 1f);
        vc.addVertex(m,  s, -s, 0f).setColor(1f, 1f, 1f, 1f).setUv(1f, 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 0f, 1f);
        vc.addVertex(m,  s,  s, 0f).setColor(1f, 1f, 1f, 1f).setUv(1f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 0f, 1f);
        vc.addVertex(m, -s,  s, 0f).setColor(1f, 1f, 1f, 1f).setUv(0f, 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0f, 0f, 1f);

        ps.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CutEffectEntity e) {
        return TEX;
    }
}