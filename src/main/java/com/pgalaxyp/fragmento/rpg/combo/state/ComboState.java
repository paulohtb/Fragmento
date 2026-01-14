package com.pgalaxyp.fragmento.rpg.combo.state;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboState(ActionKey actionKey, WeaponId weaponId, int stepIndex, int stepsTotal) {

    public ComboState {
        if (actionKey == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        if (stepsTotal <= 0) {
            throw new IllegalArgumentException();
        }
        if (stepIndex < 0 || stepIndex >= stepsTotal) {
            throw new IllegalArgumentException();
        }
    }

    public ComboState start(ActionKey actionKey, WeaponId nextWeaponId, int total) {
        if (actionKey == null || nextWeaponId == null) {
            throw new IllegalArgumentException();
        }
        return new ComboState(actionKey, nextWeaponId, 0, total);
    }

    public ComboState advance() {
        int next = Math.addExact(stepIndex, 1);
        if (next >= stepsTotal) {
            throw new IllegalArgumentException();
        }
        return new ComboState(actionKey, weaponId, next, stepsTotal);
    }
}