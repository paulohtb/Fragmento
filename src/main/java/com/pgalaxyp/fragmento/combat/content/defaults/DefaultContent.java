package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.GameContent;

public final class DefaultContent {

    public static GameContent create() {
        return new GameContent(
                DefaultActions.create(),
                DefaultCombos.create(),
                DefaultEffects.create(),
                DefaultCycles.create(),
                DefaultWeapons.create(),
                DefaultClasses.create()
        );
    }

    private DefaultContent() {}
}