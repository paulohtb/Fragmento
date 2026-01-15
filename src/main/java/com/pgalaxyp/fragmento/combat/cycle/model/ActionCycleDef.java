package com.pgalaxyp.fragmento.combat.cycle.model;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.action.model.*;

public record ActionCycleDef(ComboId comboId, ActionId actionId) {
    public ActionCycleDef {
        if (comboId == null || actionId == null) { throw new IllegalArgumentException(); }
    }
}