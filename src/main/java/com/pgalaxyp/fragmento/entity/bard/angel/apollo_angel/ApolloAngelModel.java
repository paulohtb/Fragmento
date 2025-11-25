package com.pgalaxyp.fragmento.entity.bard.angel.apollo_angel;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ApolloAngelModel extends GeoModel<ApolloAngel> {

    public ResourceLocation getModelResource(ApolloAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "geo/ApolloAngel.geo.json");
    }

    public ResourceLocation getTextureResource(ApolloAngel animatable) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", "textures/apollo_angel_texture.png");
    }

    public ResourceLocation getAnimationResource(ApolloAngel animatable) {
        return null;
    }
}