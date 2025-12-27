package com.pgalaxyp.fragmento.cosmetics.client.render;

import com.pgalaxyp.fragmento.cosmetics.client.render.model.Cube8Model;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticModels;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.RedCube8Model;
import net.minecraft.resources.ResourceLocation;

public final class CosmeticsModelsBootstrap {

    private CosmeticsModelsBootstrap() {}

    public static void bootstrap() {
        CosmeticModels.register(
                ResourceLocation.fromNamespaceAndPath("fragmento", "demo_halo"),
                new Cube8Model()
        );
        CosmeticModels.register(
                ResourceLocation.fromNamespaceAndPath("fragmento", "red_cube_head"),
                new RedCube8Model()
        );
    }
}