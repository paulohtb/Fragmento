package com.pgalaxyp.fragmento.entity.bard.projectile.guitar_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GuitarProjectileModel extends GeoModel<GuitarProjectile> {

    public ResourceLocation getModelResource(GuitarProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(GuitarProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(GuitarProjectile animatable) {
        return null;
    }
}