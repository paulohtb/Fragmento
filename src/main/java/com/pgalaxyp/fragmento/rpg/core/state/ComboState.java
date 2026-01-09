package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ComboState(
        ActionId actionId,
        WeaponId weaponId,
        int stepIndex,
        int stepsTotal
) {
    public ComboState {
        if (actionId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        if (stepsTotal <= 0) {
            throw new IllegalArgumentException();
        }
        if (stepIndex < 0 || stepIndex >= stepsTotal) {
            throw new IllegalArgumentException();
        }
    }

    public ComboState advance() {
        return new ComboState(actionId, weaponId, stepIndex + 1, stepsTotal);
    }
}