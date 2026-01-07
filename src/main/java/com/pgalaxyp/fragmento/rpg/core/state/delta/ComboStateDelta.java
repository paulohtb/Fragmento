package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;

public record ComboStateDelta(
        long actorId,
        ComboState nextCombo
) implements StateDelta {
    public ComboStateDelta {
        if (nextCombo == null) throw new IllegalArgumentException("ComboStateDelta.nextCombo");
    }
}