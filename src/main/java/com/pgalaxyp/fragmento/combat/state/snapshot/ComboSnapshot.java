package com.pgalaxyp.fragmento.combat.state.snapshot;

public record ComboSnapshot(
        int comboIndex
) {
    public ComboSnapshot {
        if (comboIndex < 0) {
            comboIndex = 0;
        }
    }

    public static ComboSnapshot idle() {
        return new ComboSnapshot(0);
    }
}