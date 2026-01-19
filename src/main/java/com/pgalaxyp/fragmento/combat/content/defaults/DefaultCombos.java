package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;
import java.util.*;

public final class DefaultCombos {
    public static void register(ContentRegistry registry) {
        var pattern = new ComboPattern(List.of(new ComboStep(0, ComboInput.PRIMARY)));
        registry.comboBase(DefaultIds.WEAPON_FLUTE, DefaultIds.COMBO_FLUTE_BASIC, pattern);
    }
    private DefaultCombos() {}
}