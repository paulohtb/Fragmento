package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import net.minecraft.resources.ResourceLocation;

public final class CosmeticsModelsBootstrap {

    private CosmeticsModelsBootstrap() {}

    public static void bootstrap() {
        CosmeticModels.register(
                ResourceLocation.fromNamespaceAndPath("fragmento", "demo_halo"),
                new Cube8Model(255, 255, 255, 255, true)
        );

        CosmeticModels.register(
                ResourceLocation.fromNamespaceAndPath("fragmento", "red_cube_head"),
                new Cube8Model(255, 0, 0, 255, false)
        );
    }
}