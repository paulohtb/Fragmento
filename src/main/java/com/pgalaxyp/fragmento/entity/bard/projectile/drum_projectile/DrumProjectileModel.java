package com.pgalaxyp.fragmento.entity.bard.projectile.drum_projectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DrumProjectileModel extends GeoModel<DrumProjectile> {

    public ResourceLocation getModelResource(DrumProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(DrumProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(DrumProjectile animatable) {
        return null;
    }
}