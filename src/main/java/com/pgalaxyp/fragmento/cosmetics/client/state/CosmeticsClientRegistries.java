package com.pgalaxyp.fragmento.cosmetics.client.state;

import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistryImpl;

public final class CosmeticsClientRegistries {

    private static volatile CosmeticRegistry REGISTRY;

    private CosmeticsClientRegistries() {}

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