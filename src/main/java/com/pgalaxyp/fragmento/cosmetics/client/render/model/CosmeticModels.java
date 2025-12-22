package com.pgalaxyp.fragmento.cosmetics.client.render.model;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CosmeticModels {

    private static final Map<ResourceLocation, CosmeticModel> BY_ID = new HashMap<>();
    private static final CosmeticModel FALLBACK = new Cube8Model();

    static {
        BY_ID.put(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "demo_halo"), new DemoHaloModel());
    }

    private CosmeticModels() {
    }

    public static CosmeticModel get(ResourceLocation cosmeticId) {
        if (cosmeticId == null) return FALLBACK;
        CosmeticModel m = BY_ID.get(cosmeticId);
        return m == null ? FALLBACK : m;
    }
}