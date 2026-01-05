package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.domain.execution.ExecutionFacts;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionKind;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionState;

public final class ExecutionLifecycleRule {

    private static final Duration FALLBACK_DURATION = Duration.ofTicks(20);

    public ExecutionState refresh(ExecutionState exec, ExecutionFacts facts, Time now) {
        if (now == null) return exec != null ? exec : ExecutionState.idle();

        ExecutionState cur = exec != null ? exec : ExecutionState.idle();

        if (facts == null) {
            if (cur.active() && now.isAfterOrEqual(cur.expectedEndAt())) {
                return cur.stop();
            }
            return cur;
        }

        if (cur.active() && !facts.hasActiveCombatEntity()) {
            return cur.stop();
        }

        if (!cur.active() && facts.hasActiveCombatEntity()) {
            long nextId = cur.execId() + 1L;
            return new ExecutionState(
                    true,
                    ExecutionKind.UNKNOWN,
                    nextId,
                    now,
                    now.plus(FALLBACK_DURATION)
            );
        }

        return cur;
    }
}