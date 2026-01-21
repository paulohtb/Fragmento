package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;

public final class DefaultContent {
    public static GameContent create() {
        var registry = new ContentRegistry();
        DefaultWeapons.register(registry);
        DefaultClasses.register(registry);
        DefaultEffects.register(registry);
        DefaultCombos.register(registry);
        DefaultSkills.register(registry);
        return registry.build();
    }

    private DefaultContent() {}
}