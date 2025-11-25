package com.pgalaxyp.fragmento.entity.bard.projectile.lute_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LuteProjectileModel extends GeoModel<LuteProjectile> {

    public ResourceLocation getModelResource(LuteProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(LuteProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(LuteProjectile animatable) {
        return null;
    }
}