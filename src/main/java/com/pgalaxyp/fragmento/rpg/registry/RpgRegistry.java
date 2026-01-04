package com.pgalaxyp.fragmento.rpg.registry;

import com.pgalaxyp.fragmento.rpg.catalyst.registry.CatalystRegistry;

public final class RpgRegistry {

    private static final CatalystRegistry CATALYSTS = new CatalystRegistry();

    public static CatalystRegistry catalysts() {
        return CATALYSTS;
    }

    private RpgRegistry() {
    }
}