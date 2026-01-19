package com.pgalaxyp.fragmento.combat.combo.api;

public record ComboStep(int index, ComboInput input) {
    public ComboStep {
        if (index < 0 || input == null) throw new IllegalArgumentException();
    }
}
