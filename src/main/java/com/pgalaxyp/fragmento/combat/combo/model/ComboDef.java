package com.pgalaxyp.fragmento.combat.combo.model;

public record ComboDef(ComboId comboId, ComboPattern pattern) {

    public ComboDef {
        if (comboId == null || pattern == null) { throw new IllegalArgumentException(); }
    }

    public int stepsTotal() { return pattern.size(); }
}