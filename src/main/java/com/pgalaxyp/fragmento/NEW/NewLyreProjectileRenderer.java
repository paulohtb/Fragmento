package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class NewLyreProjectileRenderer extends EntityRenderer<NewLyreProjectile> {

    private final NewLyreProjectileModel<NewLyreProjectile> model;

    public NewLyreProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new NewLyreProjectileModel<>(ctx.bakeLayer(NewLyreProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(NewLyreProjectile entity, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int light) {

        pose.pushPose();
        pose.translate(0.0, entity.getBbHeight() * 0.5, 0.0);
        int delay = entity.getSpawnDelayTicks();
        int stage = 2 - delay;
        float scale;
        if (stage == 0) scale = 0.2f;
        else if (stage == 1) scale = 0.4f;
        else if (stage == 2) scale = 0.6f;
        else if (stage == 3) scale = 0.8f;
        else scale = 1.0f;
        pose.scale(0.75f * scale, 0.75f * scale, 0.75f * scale);
        Vec3 mov = entity.getDeltaMovement();
        double len2 = mov.lengthSqr();
        if (len2 > 0.000001) {
            float rotYaw = (float)(Math.atan2(mov.x, mov.z) * 180.0F / Math.PI);
            float rotPitch = (float)(-(Math.atan2(mov.y, Math.sqrt(mov.x * mov.x + mov.z * mov.z)) * 180.0F / Math.PI));
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotYaw));
            pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotPitch));
        }

        int visualTick = entity.tickCount;
        model.applyTickVisibility(visualTick);

        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity))), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        pose.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NewLyreProjectile entity) {
        return ResourceLocation.fromNamespaceAndPath(
                "fragmento", "textures/entity/new_lyre_projectile_texture.png"
        );
    }
}
