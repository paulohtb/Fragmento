package com.pgalaxyp.fragmento.rpg.combo.state;

import com.pgalaxyp.fragmento.rpg.combo.model.*;

public record ComboState(ComboId comboId, int stepIndex, int stepsTotal) {

    public ComboState {
        if (comboId == null) throw new IllegalArgumentException();
        if (stepsTotal <= 0 || stepIndex < 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
    }

    public ComboState advance() {
        int next = Math.addExact(stepIndex, 1);
        if (next >= stepsTotal) throw new IllegalArgumentException();

        return new ComboState(comboId, next, stepsTotal);
    }
}