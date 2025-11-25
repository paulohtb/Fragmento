package com.pgalaxyp.fragmento.entity.timerEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TimerEntityModel extends GeoModel<TimerEntity> {

    public ResourceLocation getModelResource(TimerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/banjo.geo.json");
    }

    public ResourceLocation getTextureResource(TimerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/banjo_projectile_texture.png");
    }

    public ResourceLocation getAnimationResource(TimerEntity animatable) {
        return null;
    }
}