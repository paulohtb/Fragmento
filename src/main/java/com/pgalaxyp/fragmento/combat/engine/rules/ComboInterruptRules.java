package com.pgalaxyp.fragmento.combat.engine.rules;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;

public final class ComboInterruptRules {

    public boolean shouldInterruptCombo(WeaponInputType inputType) {
        if (inputType == null) {
            return false;
        }
        return inputType == WeaponInputType.SKILL_PRESS || inputType == WeaponInputType.SKILL_CANCEL;
    }
}