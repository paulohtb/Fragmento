package com.pgalaxyp.fragmento.entity.bard.angel.hipnos_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HipnosAngelModel extends GeoModel<HipnosAngel> {

    public ResourceLocation getModelResource(HipnosAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/hipnos_angel.geo.json");
    }

    public ResourceLocation getTextureResource(HipnosAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/hipnos_angel_texture.png");
    }

    public ResourceLocation getAnimationResource(HipnosAngel animatable) {
        return null;
    }
}