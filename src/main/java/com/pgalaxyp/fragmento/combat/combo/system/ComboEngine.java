package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.*;

public final class ComboEngine implements ComboPort {
    private final ComboRepository repo = new ComboRepository();
    private long cachedFrameId = Long.MIN_VALUE;

    @Override
    public ComboOutcome tryInput(ComboIntent intent, FrameContext frame, ComboPattern pattern) {
        Objects.requireNonNull(intent);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(pattern);

        long f = frame.frameId();
        ensureFrame(f);

        ActorId actorId = intent.actorId();
        ComboId comboId = intent.comboId();
        ComboInput input = intent.input();

        if (repo.isLocked(actorId, f)) {
            return ComboOutcome.rejected(new ComboEvent.Rejected(actorId, comboId, input, ComboRejectReason.LOCKED));
        }

        var prevOpt = repo.activeOf(actorId, f);
        if (prevOpt.isEmpty()) return startOrReject(actorId, comboId, input, f, pattern);

        ComboSnapshot prev = prevOpt.get();
        if (!prev.comboId().equals(comboId) || prev.stepsTotal() != pattern.size()) {
            repo.clear(actorId);
            resetThisFrame.put(actorId, prev);
            return startOrReject(actorId, comboId, input, f, pattern, ComboResetReason.COMBO_CHANGED);
        }

        int nextIndex = prev.stepIndex() + 1;
        if (nextIndex >= pattern.size()) {
            repo.clear(actorId);
            resetThisFrame.put(actorId, prev);
            return startOrReject(actorId, comboId, input, f, pattern, ComboResetReason.TIMEOUT);
        }

        ComboStep nextStep = pattern.step(nextIndex);
        long expires = prev.lastAcceptedFrame() + nextStep.maxGapFrames();
        if (f >= expires) {
            repo.clear(actorId);
            resetThisFrame.put(actorId, prev);
            return startOrReject(actorId, comboId, input, f, pattern, ComboResetReason.TIMEOUT);
        }

        if (nextStep.input() != input) {
            return ComboOutcome.rejected(new ComboEvent.Rejected(actorId, comboId, input, ComboRejectReason.INPUT_MISMATCH));
        }

        long nextExpires = f + nextStep.maxGapFrames();
        var next = new ComboRepository.Active(comboId, nextIndex, pattern.size(), prev.startedAtFrame(), f, nextExpires);
        repo.startOrUpdate(actorId, next);

        if (nextStep.lockFrames() > 0) repo.lock(actorId, Math.addExact(f, nextStep.lockFrames()));

        ComboSnapshot snap = next.snapshot(actorId);
        boolean finisher = nextIndex == pattern.size() - 1;

        if (finisher) {
            endedThisFrame.put(actorId, snap);
            repo.clear(actorId);
            return ComboOutcome.accepted(List.of(new ComboEvent.Completed(snap, input)));
        }

        steppedThisFrame.put(actorId, snap);
        return ComboOutcome.accepted(List.of(new ComboEvent.Advanced(snap, input)));
    }

    @Override
    public ComboTickResult tick(FrameContext frame) {
        Objects.requireNonNull(frame);
        long f = frame.frameId();
        ensureFrame(f);

        var events = new ArrayList<ComboEvent>();
        repo.cleanup(f, (actorId, a) -> {
            ComboSnapshot prev = a.snapshot(actorId);
            resetThisFrame.put(actorId, prev);
            events.add(new ComboEvent.Reset(actorId, a.comboId(), ComboResetReason.TIMEOUT));
        });

        return events.isEmpty() ? ComboTickResult.empty() : new ComboTickResult(List.copyOf(events));
    }

    @Override public Optional<ComboSnapshot> activeOf(ActorId actorId, long frameId) { return repo.activeOf(actorId, frameId); }
    @Override public List<ComboSnapshot> activeAll(long frameId) { return repo.activeAll(frameId); }
    @Override public boolean isLocked(ActorId actorId, long frameId) { return repo.isLocked(actorId, frameId); }

    private ComboOutcome startOrReject(ActorId actorId, ComboId comboId, ComboInput input, long f, ComboPattern pattern) {
        return startOrReject(actorId, comboId, input, f, pattern, null);
    }

    private ComboOutcome startOrReject(ActorId actorId, ComboId comboId, ComboInput input, long f, ComboPattern pattern, ComboResetReason resetReasonOpt) {
        ComboStep s0 = pattern.step(0);
        if (s0.input() != input) {
            var evs = resetReasonOpt == null
                    ? List.<ComboEvent>of(new ComboEvent.Rejected(actorId, comboId, input, ComboRejectReason.INPUT_MISMATCH))
                    : List.<ComboEvent>of(
                            new ComboEvent.Reset(actorId, comboId, resetReasonOpt),
                            new ComboEvent.Rejected(actorId, comboId, input, ComboRejectReason.INPUT_MISMATCH)
                    );
            return resetReasonOpt == null
                    ? ComboOutcome.rejected((ComboEvent.Rejected) evs.get(0))
                    : new ComboOutcome(false, evs);
        }

        long expires = f + s0.maxGapFrames();
        var a = new ComboRepository.Active(comboId, 0, pattern.size(), f, f, expires);
        repo.startOrUpdate(actorId, a);
        if (s0.lockFrames() > 0) repo.lock(actorId, Math.addExact(f, s0.lockFrames()));

        ComboSnapshot snap = a.snapshot(actorId);
        startedThisFrame.put(actorId, snap);

        var started = new ComboEvent.Started(snap, input);
        if (pattern.size() == 1) {
            endedThisFrame.put(actorId, snap);
            repo.clear(actorId);
            var completed = new ComboEvent.Completed(snap, input);
            return ComboOutcome.accepted(resetReasonOpt == null ? List.of(started, completed) : List.of(new ComboEvent.Reset(actorId, comboId, resetReasonOpt), started, completed));
        }

        return ComboOutcome.accepted(resetReasonOpt == null ? List.of(started) : List.of(new ComboEvent.Reset(actorId, comboId, resetReasonOpt), started));
    }

    private void ensureFrame(long frameId) {
        if (frameId == cachedFrameId) return;
        cachedFrameId = frameId;
        startedThisFrame.clear();
        steppedThisFrame.clear();
        endedThisFrame.clear();
        resetThisFrame.clear();
    }
}