package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.*;

public record WeaponDef(WeaponId id, WeaponSpec spec, ActionKey actionKey) {

    public WeaponDef {
        if (id == null || spec == null || actionKey == null) {
            throw new IllegalArgumentException();
        }
    }
}