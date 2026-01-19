package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.emit.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class TimedSequenceActionRuntime implements ActionRuntime {

    private final List<EffectStep> steps;
    private final int windowFrames;
    private long lastEmitFrame = Long.MIN_VALUE;
    private int nextIndex;
    private boolean started;
    private boolean done;

    public TimedSequenceActionRuntime(TimedSequenceActionPlan plan) {
        this.steps = plan.steps();
        this.windowFrames = plan.windowFrames();
    }

    @Override
    public ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request) {
        if (done) return ActionOutcome.finished(List.of());
        if (request instanceof ActionRequest.Cancel) { done = true; return ActionOutcome.finished(List.of()); }

        if (request instanceof ActionRequest.Start) {
            started = true;
            lastEmitFrame = frameId;
            nextIndex = 0;
            return emit();
        }

        if (!(request instanceof ActionRequest.Tick) || !started) return ActionOutcome.reject();

        long delta = frameId - lastEmitFrame;
        if (delta < 0 || delta > windowFrames * 2L) { done = true; return ActionOutcome.finished(List.of()); }
        if (delta < windowFrames) return ActionOutcome.running(List.of());

        lastEmitFrame = frameId;
        return emit();
    }

    private ActionOutcome emit() {
        if (nextIndex >= steps.size()) { done = true; return ActionOutcome.finished(List.of()); }
        var intent = steps.get(nextIndex++).intent();
        boolean finished = nextIndex >= steps.size();
        if (finished) done = true;
        var em = EffectIntentEmission.of(intent);
        return finished ? ActionOutcome.finished(List.of(em)) : ActionOutcome.running(List.of(em));
    }
}