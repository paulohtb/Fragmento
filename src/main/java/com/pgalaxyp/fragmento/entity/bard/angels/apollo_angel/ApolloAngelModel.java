package com.pgalaxyp.fragmento.entity.bard.angels.apollo_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ApolloAngelModel extends GeoModel<ApolloAngel> {

    public ResourceLocation getModelResource(ApolloAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/apollo_angel.geo.json");
    }

    public ResourceLocation getTextureResource(ApolloAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "texture/apollo_angel_texture.png");
    }

    public ResourceLocation getAnimationResource(ApolloAngel animatable) {
        return null;
    }
}