package com.pgalaxyp.fragmento.feature.bard_class.client.model.spirit;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl.FluteVortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluteVortexGeoModel extends GeoModel<FluteVortex> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath("fragmento", "geo/flute_vortex.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/flute_vortex.png");
    private static final ResourceLocation ANIM =
            ResourceLocation.fromNamespaceAndPath("fragmento", "animations/flute_vortex.animation.json");

    @Override
    public ResourceLocation getModelResource(FluteVortex animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FluteVortex animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FluteVortex animatable) {
        return ANIM;
    }
}
