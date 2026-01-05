package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionState;

public final class ExecutionGateRule {

    public boolean allowAction(ExecutionState exec) {
        if (exec == null) return true;
        return !exec.active();
    }
}