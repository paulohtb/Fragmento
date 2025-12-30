package com.pgalaxyp.fragmento.combat.state.action;

import com.pgalaxyp.fragmento.combat.domain.action.ActionOutcome;

public final class ActionResultState {

    private ActionOutcome outcome;
    private boolean resolved;

    public ActionOutcome outcome() {
        return outcome;
    }

    public boolean resolved() {
        return resolved;
    }

    public void resolve(ActionOutcome outcome) {
        this.outcome = outcome;
        this.resolved = true;
    }

    public void clear() {
        this.outcome = null;
        this.resolved = false;
    }
}