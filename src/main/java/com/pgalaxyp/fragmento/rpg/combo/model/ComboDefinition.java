package com.pgalaxyp.fragmento.rpg.combo.model;

import java.util.*;

public record ComboDefinition(ComboId comboId, ComboPattern pattern) {

    public ComboDefinition {
        if (comboId == null || pattern == null) throw new IllegalArgumentException();
    }

    public int stepsTotal() { return pattern.size(); }
}