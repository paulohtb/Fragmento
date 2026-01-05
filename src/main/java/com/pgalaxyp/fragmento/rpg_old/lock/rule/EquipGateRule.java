package com.pgalaxyp.fragmento.rpg_old.lock.rule;

import com.pgalaxyp.fragmento.rpg_old.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg_old.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.LoadoutState;

public final class EquipGateRule {

    public boolean allowAttack(
            LoadoutState loadout,
            AttackIntent intent
    ) {
        if (loadout == null || intent == null) {
            return false;
        }
        return loadout.valid();
    }

    public boolean allowAbility(
            LoadoutState loadout,
            AbilityIntent intent
    ) {
        if (loadout == null || intent == null) {
            return false;
        }
        return loadout.valid();
    }
}