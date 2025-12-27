package com.pgalaxyp.fragmento.cosmetics.client.render;

import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistryImpl;

public final class CosmeticsClientRegistryAccess {

    private static volatile CosmeticRegistry REGISTRY;

    private CosmeticsClientRegistryAccess() {
    }

    public static void setRegistry(CosmeticRegistry registry) {
        REGISTRY = registry;
    }

    public static CosmeticRegistry registry() {
        CosmeticRegistry r = REGISTRY;
        if (r == null) {
            r = new CosmeticRegistryImpl();
            REGISTRY = r;
        }
        return r;
    }
}