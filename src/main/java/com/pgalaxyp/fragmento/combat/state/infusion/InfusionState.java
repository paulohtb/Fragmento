package com.pgalaxyp.fragmento.combat.state.infusion;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record InfusionState(
        InfusionSpec spec,
        CombatTime armedAt
) {

    public static InfusionState empty() {
        return new InfusionState(null, null);
    }

    public boolean isArmed() {
        return spec != null && armedAt != null;
    }

    public boolean isExpired(CombatTime now) {
        if (!isArmed() || now == null || spec.expiresAfter() == null) {
            return false;
        }
        return now.isAfterOrEqual(armedAt.plus(spec.expiresAfter()));
    }

    public InfusionState arm(InfusionSpec spec, CombatTime now) {
        if (spec == null || now == null) {
            return this;
        }
        return new InfusionState(spec, now);
    }

    public ActionDefinition infusedActionOrNull() {
        return spec != null ? spec.infusedAction() : null;
    }

    public InfusionState clear() {
        return empty();
    }
}