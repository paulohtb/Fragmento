package com.pgalaxyp.fragmento.rpg.combo.model;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboDefinition(ActionKey actionKey, ComboPattern pattern) {

    public ComboDefinition {
        if (actionKey == null || pattern == null) {
            throw new IllegalArgumentException();
        }
    }

    public int stepsTotal() {
        return pattern.size();
    }
}