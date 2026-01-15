package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.content.defaults.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;

public final class DefaultContent {
    public static GameContent create() {
        ContentRegistry registry = new ContentRegistry();
        DefaultWeapons.register(registry);
        DefaultEffects.register(registry);
        DefaultActions.register(registry);
        DefaultCombos.register(registry);
        DefaultCycles.register(registry);
        return registry.build();
    }
    private DefaultContent() {}
}