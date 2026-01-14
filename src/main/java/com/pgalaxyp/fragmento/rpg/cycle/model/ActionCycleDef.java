package com.pgalaxyp.fragmento.rpg.cycle.model;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.action.model.*;

public record ActionCycleDef(ComboId comboId, ActionId actionId) {
    public ActionCycleDef { if (comboId == null || actionId == null) throw new IllegalArgumentException(); }
}