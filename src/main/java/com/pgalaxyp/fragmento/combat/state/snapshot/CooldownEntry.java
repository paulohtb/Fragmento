package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

import java.util.Objects;

public record CooldownEntry(
        CombatTime endsAt
) {
    public CooldownEntry {
        endsAt = Objects.requireNonNull(endsAt, "endsAt");
    }

    public boolean isActiveAt(CombatTime now) {
        if (now == null) {
            return false;
        }
        return now.isBefore(endsAt);
    }
}