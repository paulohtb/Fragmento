package com.pgalaxyp.fragmento.entity.bard.angels.hercules_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerculesAngelModel extends GeoModel<HerculesAngel> {

    public ResourceLocation getModelResource(HerculesAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/hercules_angel.geo.json");
    }

    public ResourceLocation getTextureResource(HerculesAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/hercules_angel_texture.png");
    }

    public ResourceLocation getAnimationResource(HerculesAngel animatable) {
        return null;
    }
}