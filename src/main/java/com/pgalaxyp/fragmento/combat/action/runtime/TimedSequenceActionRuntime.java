package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public final class TimedSequenceActionRuntime implements ActionRuntime {
    private final List<EffectStep> steps;
    private final int windowFrames;
    private long startFrame = Long.MIN_VALUE;
    private long lastEmitFrame = Long.MIN_VALUE;
    private int nextIndex;
    private boolean done;

    public TimedSequenceActionRuntime(TimedSequenceActionPlan plan) {
        this.steps = plan.steps();
        this.windowFrames = plan.windowFrames();
    }

    @Override
    public ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request) {
        if (done) return ActionOutcome.finished(List.of());
        if (request instanceof ActionRequest.Cancel) {
            done = true;
            return ActionOutcome.finished(List.of());
        }
        if (request instanceof ActionRequest.Start) {
            startFrame = frameId;
            lastEmitFrame = frameId;
            nextIndex = 0;
            return emit(frameId);
        }
        if (!(request instanceof ActionRequest.Tick)) {
            return ActionOutcome.reject();
        }
        if (startFrame == Long.MIN_VALUE) {
            return ActionOutcome.reject();
        }

        long delta = frameId - lastEmitFrame;
        if (delta < 0 || delta > windowFrames * 2L) {
            done = true;
            return ActionOutcome.finished(List.of());
        }
        if (delta < windowFrames) {
            return ActionOutcome.running(List.of());
        }
        lastEmitFrame = frameId;

        return emit(frameId);
    }

    private ActionOutcome emit(long frameId) {
        if (nextIndex >= steps.size()) {
            done = true;
            return ActionOutcome.finished(List.of());
        }

        EffectIntent intent = steps.get(nextIndex++).intent();
        boolean finished = nextIndex >= steps.size();
        if (finished) done = true;

        return ActionOutcome.success(List.of(intent), finished);
    }
}