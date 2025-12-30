package com.pgalaxyp.fragmento.combat.state.action;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record ActionProgress(
        CombatTime startedAt,
        CombatTime endsAt,
        CombatTime comboWindowEndsAt
) {

    public static ActionProgress create(CombatTime now, Duration total, Duration comboWindow) {
        long start = now.ticks();
        long end = start + Math.max(0, total.ticks());
        long windowEnd = start + Math.max(0, comboWindow.ticks());
        return new ActionProgress(new CombatTime(start), new CombatTime(end), new CombatTime(windowEnd));
    }

    public boolean isActive(CombatTime now) {
        return now.ticks() < endsAt.ticks();
    }

    public boolean isComboWindowOpen(CombatTime now) {
        return now.ticks() <= comboWindowEndsAt.ticks();
    }

    public long remainingTicks(CombatTime now) {
        long rem = endsAt.ticks() - now.ticks();
        return Math.max(0, rem);
    }
}