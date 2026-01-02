package com.pgalaxyp.fragmento.combat.rule.skill;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

public final class InfusedRule {

    private final int durationTicks;

    public InfusedRule(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public void arm(ServerCombatState state, CombatTime now) {
        if (state == null || now == null) {
            return;
        }

        CombatTime until = CombatTime.ofTicks(now.ticks() + durationTicks);
        state.infusion().arm(until);

        state.bumpVersion();
    }

    public boolean consumeIfArmed(ServerCombatState state) {
        if (state == null) {
            return false;
        }
        if (!state.infusion().armed()) {
            return false;
        }

        state.infusion().consume();
        state.bumpVersion();
        return true;
    }
}