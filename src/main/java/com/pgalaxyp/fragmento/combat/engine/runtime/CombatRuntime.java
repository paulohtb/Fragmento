package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

public final class CombatRuntime {

    private final CombatClock clock;
    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;

    private ServerCombatState state;

    public CombatRuntime(
            CombatClock clock,
            CombatEngine combatEngine,
            AbilityEngine abilityEngine,
            ServerCombatState initial
    ) {
        this.clock = clock;
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
        this.state = initial;
    }

    public void onAttackIntent(AttackIntent intent) {
        CombatTime now = clock.now();
        state = combatEngine.applyAttack(state, intent, now);
    }

    public void onAbilityIntent(AbilityIntent intent) {
        CombatTime now = clock.now();
        state = abilityEngine.apply(state, intent, now);
    }

    public void tick() {
        CombatTime now = clock.now();
        state = combatEngine.tick(state, now);
        state = abilityEngine.tick(state, now);
    }

    public ServerCombatState state() {
        return state;
    }
}