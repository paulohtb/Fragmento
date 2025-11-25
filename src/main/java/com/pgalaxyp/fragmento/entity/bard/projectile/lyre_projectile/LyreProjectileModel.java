package com.pgalaxyp.fragmento.entity.bard.projectile.lyre_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LyreProjectileModel extends GeoModel<LyreProjectile> {

    public ResourceLocation getModelResource(LyreProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(LyreProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/lira_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(LyreProjectile animatable) {
        return null;
    }
}