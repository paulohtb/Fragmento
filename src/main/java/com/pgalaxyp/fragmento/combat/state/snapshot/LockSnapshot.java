package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record LockSnapshot(
        ActionKind actionKind,
        CombatTime actionEndsAt,
        CombatTime itemSwapLockedUntil
) {}