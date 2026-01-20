package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final Map<ActorId, ComboState> byActor = new HashMap<>();

    @Override
    public ComboDecision decide(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(input);

        ComboState prev = byActor.get(actorId);
        if (prev == null) return startOrReject(actorId, comboId, pattern, input);

        if (!prev.comboId.equals(comboId) || prev.stepsTotal != pattern.size()) {
            return startOrReset(actorId, comboId, pattern, input, () -> byActor.remove(actorId));
        }

        int next = prev.stepIndex + 1;
        if (next < pattern.size() && pattern.step(next).input() == input) {
            return proposed(actorId, comboId, pattern, next);
        }

        if (pattern.step(0).input() == input) {
            return proposed(actorId, comboId, pattern, 0);
        }

        return ComboDecision.reset(comboId, () -> byActor.remove(actorId));
    }

    private ComboDecision startOrReject(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input) {
        return pattern.step(0).input() == input ? proposed(actorId, comboId, pattern, 0) : ComboDecision.reject();
    }

    private ComboDecision startOrReset(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input, Runnable clear) {
        return pattern.step(0).input() == input ? proposed(actorId, comboId, pattern, 0) : ComboDecision.reset(comboId, clear);
    }

    private ComboDecision.Proposed proposed(ActorId actorId, ComboId comboId, ComboPattern pattern, int index) {
        boolean start = index == 0;
        boolean end = index == pattern.size() - 1;
        ComboStep step = pattern.step(index);

        Runnable commit = end
                ? () -> byActor.remove(actorId)
                : () -> byActor.put(actorId, new ComboState(comboId, index, pattern.size()));

        return new ComboDecision.Proposed(comboId, index, pattern.size(), step, start, end, commit);
    }

    private record ComboState(ComboId comboId, int stepIndex, int stepsTotal) {
        private ComboState {
            Objects.requireNonNull(comboId);
            if (stepsTotal <= 0 || stepIndex < 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
        }
    }
}