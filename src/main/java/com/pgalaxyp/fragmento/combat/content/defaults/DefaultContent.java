package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;

public final class DefaultContent {
    public static GameContent create() {
        var registry = new ContentRegistry();
        DefaultWeapons.register(registry);
        DefaultClasses.register(registry);
        DefaultEffects.register(registry);
        DefaultCombos.register(registry);
        return registry.build();
    }
    private DefaultContent() {}
}
