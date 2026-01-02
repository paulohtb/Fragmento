package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record ActionLockState(
        String actionKind,
        CombatTime endsAt,
        CombatTime itemSwapLockedUntil
) {
    public static ActionLockState idle() {
        CombatTime zero = CombatTime.ofTicks(0L);
        return new ActionLockState("", zero, zero);
    }

    public boolean active(CombatTime now) {
        if (now == null) {
            return false;
        }
        return now.isBefore(endsAt);
    }
}