package com.pgalaxyp.fragmento.combat.state.infusion;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionId;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public final class InfusionState {

    private InfusionSpec spec;
    private CombatTime armedAt;
    private CombatTime expiresAt;

    public boolean isArmed() {
        return spec != null;
    }

    public InfusionSpec spec() {
        return spec;
    }

    public void arm(InfusionSpec spec, CombatTime now) {
        this.spec = spec;
        this.armedAt = now;
        if (spec.expiresAfter() != null) {
            this.expiresAt = now.plus(spec.expiresAfter());
        } else {
            this.expiresAt = null;
        }
    }

    public boolean isExpired(CombatTime now) {
        if (expiresAt == null) {
            return false;
        }
        return now.isAfterOrEqual(expiresAt);
    }

    public ActionDefinition consume() {
        if (spec == null) {
            return null;
        }
        ActionDefinition action = spec.infusedAction();
        clear();
        return action;
    }

    public void clear() {
        spec = null;
        armedAt = null;
        expiresAt = null;
    }
}