package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

final class DefaultCombos {

    static ComboCatalog create() {
        ComboPattern pattern = new ComboPattern(List.of(
                new ComboStep(0, ComboInput.PRIMARY)
        ));

        Map<WeaponId, ComboCatalog.Entry> map = Map.of(
                DefaultIds.WEAPON_FLUTE,
                new ComboCatalog.Entry(DefaultIds.COMBO_FLUTE, pattern)
        );

        return new ComboCatalog(map);
    }
}