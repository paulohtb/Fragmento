package com.pgalaxyp.fragmento.combat.rule.skill;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

public final class InfusedSkillRule {

    private final int durationTicks;

    public InfusedSkillRule(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public void arm(ServerCombatState state, CombatTime now) {
        if (state == null || now == null) {
            return;
        }
        state.infusion().arm(
                CombatTime.ofTicks(now.ticks() + durationTicks)
        );
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