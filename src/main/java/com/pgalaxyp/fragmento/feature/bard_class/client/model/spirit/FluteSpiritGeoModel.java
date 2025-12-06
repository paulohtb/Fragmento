package com.pgalaxyp.fragmento.feature.bard_class.client.model.spirit;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl.FluteSpirit;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluteSpiritGeoModel extends GeoModel<FluteSpirit> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath("fragmento", "geo/flute_spirit.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/flute_spirit.png");
    private static final ResourceLocation ANIM =
            ResourceLocation.fromNamespaceAndPath("fragmento", "animations/flute_spirit.animation.json");

    @Override
    public ResourceLocation getModelResource(FluteSpirit animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FluteSpirit animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FluteSpirit animatable) {
        return ANIM;
    }
}
