package com.pgalaxyp.fragmento.combat.engine.registry;

public final class FragmentoCombatRegistries {

    private static final CatalystRegistry CATALYSTS = new CatalystRegistry();

    public static CatalystRegistry catalysts() {
        return CATALYSTS;
    }

    private FragmentoCombatRegistries() {
    }
}