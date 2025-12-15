package com.pgalaxyp.fragmento.content.bard.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public final class VortexModel<T extends GeoAnimatable> extends GeoModel<T> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath("fragmento", "geo/entity/wind_vortex.geo.json");

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/wind_vortex.png");

    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath("fragmento", "animations/entity/wind_vortex.animation.json");

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION;
    }
}
