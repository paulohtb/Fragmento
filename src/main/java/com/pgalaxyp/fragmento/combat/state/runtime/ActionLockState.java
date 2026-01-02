package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record ActionLockState(
        ActionKind actionKind,
        CombatTime endsAt,
        CombatTime itemSwapLockedUntil
) {

    public static ActionLockState idle() {
        CombatTime zero = CombatTime.ofTicks(0L);
        return new ActionLockState(null, zero, zero);
    }

    public boolean active(CombatTime now) {
        return now != null && now.isBefore(endsAt);
    }
}