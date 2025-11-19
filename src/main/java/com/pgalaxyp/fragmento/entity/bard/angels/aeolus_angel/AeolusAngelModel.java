package com.pgalaxyp.fragmento.entity.bard.angels.aeolus_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AeolusAngelModel extends GeoModel<AeolusAngel> {

    public ResourceLocation getModelResource(AeolusAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(AeolusAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(AeolusAngel animatable) {
        return null;
    }
}