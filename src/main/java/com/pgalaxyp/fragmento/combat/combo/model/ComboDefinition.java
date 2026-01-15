package com.pgalaxyp.fragmento.combat.combo.model;

public record ComboDefinition(ComboId comboId, ComboPattern pattern) {

    public ComboDefinition {
        if (comboId == null || pattern == null) throw new IllegalArgumentException();
    }

    public int stepsTotal() { return pattern.size(); }
}