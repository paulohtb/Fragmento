package com.pgalaxyp.fragmento.NEW.newnew;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SimpleProjectileRenderer<T extends Entity> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/new_flute_projectile_texture.png");

    public SimpleProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light) {
        pose.pushPose();
        pose.scale(0.3f, 0.3f, 0.3f);
        super.render(entity, yaw, partialTicks, pose, buffer, light);
        pose.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}