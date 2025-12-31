package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

import java.util.Objects;

public record ChannelSnapshot(
        Phase phase,
        long targetEntityId,
        CombatTime endsAtTick
) {
    public ChannelSnapshot {
        phase = phase == null ? Phase.CASTING : phase;
        endsAtTick = Objects.requireNonNullElseGet(endsAtTick, () -> CombatTime.ofTicks(0L));
        targetEntityId = Math.max(0L, targetEntityId);
    }

    public static ChannelSnapshot idle() {
        return new ChannelSnapshot(Phase.CASTING, 0L, CombatTime.ofTicks(0L));
    }

    public boolean isActiveAt(CombatTime now) {
        if (now == null) {
            return false;
        }
        return now.isBefore(endsAtTick);
    }
}