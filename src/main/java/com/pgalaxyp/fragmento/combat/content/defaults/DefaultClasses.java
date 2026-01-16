package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public final class DefaultClasses {
    public static final ClassId DEFAULT = new ClassId("class.default");

    public static void register(ContentRegistry registry) {
        registry.defaults(SpawnDefaults.of(DEFAULT, DefaultWeapons.FLUTE, 20, 20));
    }

    private DefaultClasses() {}
}