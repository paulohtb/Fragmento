package com.pgalaxyp.fragmento.combat.rule.gate;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;

public final class EquipGateRule {

    public boolean allowAttack(
            LoadoutRuntimeState loadout,
            AttackIntent intent
    ) {
        if (loadout == null || intent == null) {
            return false;
        }
        return loadout.valid();
    }

    public boolean allowAbility(
            LoadoutRuntimeState loadout,
            AbilityIntent intent
    ) {
        if (loadout == null || intent == null) {
            return false;
        }
        return loadout.valid();
    }
}