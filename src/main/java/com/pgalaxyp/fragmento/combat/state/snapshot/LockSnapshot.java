package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record LockSnapshot(
        String actionKind,
        CombatTime actionEndsAt,
        CombatTime itemSwapLockedUntil
) {}