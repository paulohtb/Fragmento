package com.pgalaxyp.fragmento.entity.violinProjectile;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ViolinProjectileModel extends GeoModel<ViolinProjectile> {

    public ResourceLocation getModelResource(ViolinProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(ViolinProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(ViolinProjectile animatable) {
        return null;
    }
}