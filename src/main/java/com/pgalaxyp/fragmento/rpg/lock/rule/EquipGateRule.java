package com.pgalaxyp.fragmento.rpg.lock.rule;

import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.state.runtime.LoadoutState;

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