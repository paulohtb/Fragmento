package com.pgalaxyp.fragmento.entity.bard.angels.orpheus_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OrpheusAngelModel extends GeoModel<OrpheusAngel> {

    public ResourceLocation getModelResource(OrpheusAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/orpheus_angel.geo.json");
    }

    public ResourceLocation getTextureResource(OrpheusAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/orpheus_angel_texture.png");
    }

    public ResourceLocation getAnimationResource(OrpheusAngel animatable) {
        return null;
    }
}