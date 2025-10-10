package com.pgalaxyp.fragmento.entity.lira_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LiraProjectileModel extends GeoModel<LiraProjectile> {

    public ResourceLocation getModelResource(LiraProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(LiraProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/lira_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(LiraProjectile animatable) {
        return null;
    }
}