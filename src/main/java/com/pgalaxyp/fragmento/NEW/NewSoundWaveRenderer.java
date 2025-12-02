package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class NewSoundWaveRenderer extends EntityRenderer<NewSoundWaveEntity> {

    private final NewSoundWaveModel<NewSoundWaveEntity> model;

    public NewSoundWaveRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new NewSoundWaveModel<>(ctx.bakeLayer(NewSoundWaveModel.LAYER_LOCATION));
    }

    @Override
    public void render(NewSoundWaveEntity entity, float yaw, float partialTicks,
                       PoseStack pose, MultiBufferSource buffer, int light) {

        pose.pushPose();

        pose.translate(0.0, entity.getBbHeight() * 0.5, 0.0);

        float scale = entity.getWaveScale(partialTicks);
        pose.scale(scale, scale, scale);

        Vec3 mov = entity.getDeltaMovement();
        double len2 = mov.lengthSqr();
        if (len2 > 0.000001) {
            float rotYaw = (float)(Math.atan2(mov.x, mov.z) * 180.0F / Math.PI);
            float rotPitch = (float)(-(Math.atan2(mov.y, Math.sqrt(mov.x * mov.x + mov.z * mov.z)) * 180.0F / Math.PI));
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotYaw));
            pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotPitch));
        }

        int stage = entity.getStage();
        model.applyStageVisibility(stage);

        model.renderToBuffer(
                pose,
                buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity))),
                light,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        pose.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NewSoundWaveEntity entity) {
        return ResourceLocation.fromNamespaceAndPath
                ("fragmento", "textures/entity/sound_wave_texture.png");
    }
}
