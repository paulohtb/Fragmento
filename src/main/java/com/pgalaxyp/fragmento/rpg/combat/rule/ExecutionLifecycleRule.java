package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.domain.execution.ExecutionFacts;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionState;

public final class ExecutionLifecycleRule {

    public ExecutionState refresh(ExecutionState exec, ExecutionFacts facts, Time now) {
        if (now == null) return exec != null ? exec : ExecutionState.idle();

        ExecutionState cur = exec != null ? exec : ExecutionState.idle();

        if (cur.active() && cur.endsAt() != null && now.isAfterOrEqual(cur.endsAt())) {
            return cur.stop();
        }

        if (facts != null && cur.active() && !facts.hasActiveCombatEntity()) {
            return cur.stop();
        }

        return cur;
    }
}