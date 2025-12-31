package com.pgalaxyp.fragmento.combat.rule.validation;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;

public final class ComboInterruptRule {

    public boolean shouldInterruptCombo(WeaponInputType inputType) {
        if (inputType == null) {
            return false;
        }
        return inputType == WeaponInputType.SKILL_PRESS || inputType == WeaponInputType.SKILL_CANCEL;
    }
}