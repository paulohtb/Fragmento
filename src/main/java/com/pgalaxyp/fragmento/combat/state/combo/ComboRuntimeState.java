package com.pgalaxyp.fragmento.combat.state.combo;

import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboStep;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public final class ComboRuntimeState {

    private ComboDefinition combo;
    private int stepIndex;

    private ComboProgressState state = ComboProgressState.BROKEN;

    private CombatTime lastHitTime;
    private CombatTime windowEndsAt;

    private Duration maxGapBetweenHits;

    public void bind(ComboDefinition combo, Duration maxGapBetweenHits) {
        this.combo = combo;
        this.maxGapBetweenHits = maxGapBetweenHits;
        reset();
    }

    public ComboDefinition combo() {
        return combo;
    }

    public int stepIndex() {
        return stepIndex;
    }

    public ComboProgressState state() {
        return state;
    }

    public ComboStep currentStep() {
        if (combo == null) {
            return null;
        }
        return combo.step(stepIndex);
    }

    public void reset() {
        stepIndex = 0;
        state = ComboProgressState.READY;
        lastHitTime = null;
        windowEndsAt = null;
    }

    public void onActionStarted(CombatTime now) {
        if (combo == null) {
            return;
        }
        if (state == ComboProgressState.BROKEN) {
            reset();
        }
        if (maxGapBetweenHits != null && lastHitTime != null) {
            long gap = now.ticks() - lastHitTime.ticks();
            if (gap > maxGapBetweenHits.ticks()) {
                reset();
            }
        }
        state = ComboProgressState.WAITING_HIT_CONFIRMATION;
        windowEndsAt = null;
    }

    public void onHitConfirmed(CombatTime now) {
        if (combo == null) {
            return;
        }
        lastHitTime = now;
        state = ComboProgressState.WINDOW_OPEN;
        long ends = now.ticks() + (maxGapBetweenHits == null ? 0 : maxGapBetweenHits.ticks());
        windowEndsAt = new CombatTime(ends);
    }

    public void onMissOrNoHit(CombatTime now) {
        reset();
    }

    public void onInterrupted(CombatTime now) {
        reset();
    }

    public boolean isWindowOpen(CombatTime now) {
        if (state != ComboProgressState.WINDOW_OPEN) {
            return false;
        }
        if (windowEndsAt == null) {
            return false;
        }
        return now.ticks() <= windowEndsAt.ticks();
    }

    public void tick(CombatTime now) {
        if (state != ComboProgressState.WINDOW_OPEN) {
            return;
        }
        if (!isWindowOpen(now)) {
            reset();
        }
    }

    public void advanceAfterConfirmedHit() {
        if (combo == null) {
            return;
        }
        if (state != ComboProgressState.WINDOW_OPEN) {
            return;
        }
        int next = stepIndex + 1;
        if (next >= combo.size()) {
            stepIndex = 0;
        } else {
            stepIndex = next;
        }
        state = ComboProgressState.READY;
        windowEndsAt = null;
    }
}