package com.pgalaxyp.fragmento.rpg.combo.api;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public sealed interface ComboDecision permits ComboDecision.Accept, ComboDecision.Reset, ComboDecision.Reject {

    record Accept(ActionKey actionKey, WeaponId weaponId, int stepIndex, int stepsTotal, ComboStep step) implements ComboDecision {
        public Accept {
            if (actionKey == null || weaponId == null || step == null) {
                throw new IllegalArgumentException();
            }
            if (stepIndex < 0) {
                throw new IllegalArgumentException();
            }
            if (stepsTotal <= 0) {
                throw new IllegalArgumentException();
            }
            if (stepIndex >= stepsTotal) {
                throw new IllegalArgumentException();
            }
            if (step.index() != stepIndex) {
                throw new IllegalArgumentException();
            }
        }
    }

    record Reset() implements ComboDecision {}

    record Reject() implements ComboDecision {}

    static ComboDecision reset() {
        return new Reset();
    }

    static ComboDecision reject() {
        return new Reject();
    }

    static ComboDecision accept(ActionKey actionKey, WeaponId weaponId, int stepIndex, int stepsTotal, ComboStep step) {
        return new Accept(actionKey, weaponId, stepIndex, stepsTotal, step);
    }
}