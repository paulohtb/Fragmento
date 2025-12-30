package com.pgalaxyp.fragmento.cosmetic.client.render.model;

import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;

public final class CosmeticModels {

    private static final ConcurrentHashMap<ResourceLocation, CosmeticModel> BY_ID =
            new ConcurrentHashMap<>();
    private static final CosmeticModel FALLBACK =
            new Cube8Model(255, 255, 255, 255, true);

    private CosmeticModels() {}

    public static void register(ResourceLocation cosmeticId, CosmeticModel model) {
        if (cosmeticId == null) {
            return;
        }
        if (model == null) {
            return;
        }
        BY_ID.put(cosmeticId, model);
    }

    public static CosmeticModel get(ResourceLocation cosmeticId) {
        if (cosmeticId == null) {
            return FALLBACK;
        }
        CosmeticModel m = BY_ID.get(cosmeticId);
        return m == null ? FALLBACK : m;
    }
}