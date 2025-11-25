package com.pgalaxyp.fragmento.entity.bard.projectile.flute_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluteProjectileModel extends GeoModel<FluteProjectile> {

    public ResourceLocation getModelResource(FluteProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(FluteProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(FluteProjectile animatable) {
        return null;
    }
}