package com.pgalaxyp.fragmento.rpg_old.combat.rule;

import com.pgalaxyp.fragmento.rpg_old.state.runtime.ExecutionState;

public final class ExecutionGateRule {

    public boolean allowAction(ExecutionState exec) {
        if (exec == null) return true;
        return !exec.active();
    }
}