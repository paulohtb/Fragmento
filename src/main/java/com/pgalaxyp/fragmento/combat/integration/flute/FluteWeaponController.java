package com.pgalaxyp.fragmento.combat.integration.flute;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponCombatRuntime;

public final class FluteWeaponController {

    private final WeaponCombatRuntime runtime;

    public FluteWeaponController(WeaponCombatRuntime runtime) {
        this.runtime = runtime;
    }

    public void tick() {
        runtime.tick();
    }

    public void onPrimaryAttack() {
        runtime.onInput(WeaponInput.primaryAttack());
    }

    public void onSkillPress(int skillId) {
        runtime.onInput(WeaponInput.skillPress(skillId));
    }

    public void onSkillCancel(int skillId) {
        runtime.onInput(WeaponInput.skillCancel(skillId));
    }

    public void onHitConfirmed(int actionLocalId) {
        runtime.onHitConfirmed(actionLocalId);
    }

    public void interruptAll() {
        runtime.interruptAll();
    }
}