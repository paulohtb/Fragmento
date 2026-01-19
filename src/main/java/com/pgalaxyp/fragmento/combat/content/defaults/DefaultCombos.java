package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;
import java.util.*;

public final class DefaultCombos {
    public static void register(ContentRegistry registry) {
        var pattern = new ComboPattern(List.of(
                new ComboStep(0, ComboInput.PRIMARY),
                new ComboStep(1, ComboInput.PRIMARY),
                new ComboStep(2, ComboInput.PRIMARY)
        ));

        registry.comboBase(DefaultIds.WEAPON_FLUTE, DefaultIds.COMBO_FLUTE_BASIC, pattern);

        registry.comboAbilities(
                DefaultIds.COMBO_FLUTE_BASIC,
                List.of(
                        DefaultIds.ABILITY_FLUTE,
                        DefaultIds.ABILITY_FLUTE,
                        DefaultIds.ABILITY_FLUTE
                )
        );
    }

    private DefaultCombos() {}
}
