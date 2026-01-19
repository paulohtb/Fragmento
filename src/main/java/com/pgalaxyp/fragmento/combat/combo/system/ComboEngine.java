package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final Map<ActorId, ComboState> byActor = new HashMap<>();

    @Override
    public ComboResult decide(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(input);

        ComboState prev = byActor.get(actorId);

        if (prev == null) return startOrReject(actorId, comboId, pattern, input);

        if (!prev.comboId.equals(comboId) || prev.stepsTotal != pattern.size()) {
            byActor.remove(actorId);
            return startOrReset(actorId, comboId, pattern, input);
        }

        int next = prev.stepIndex + 1;
        if (next < pattern.size() && pattern.step(next).input() == input) {
            return progress(actorId, comboId, pattern, next);
        }

        if (pattern.step(0).input() == input) {
            return progress(actorId, comboId, pattern, 0);
        }

        byActor.remove(actorId);
        return ComboResult.reset(comboId);
    }

    private ComboResult startOrReject(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input) {
        return pattern.step(0).input() == input ? progress(actorId, comboId, pattern, 0) : ComboResult.reject();
    }

    private ComboResult startOrReset(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input) {
        return pattern.step(0).input() == input ? progress(actorId, comboId, pattern, 0) : ComboResult.reset(comboId);
    }

    private ComboResult progress(ActorId actorId, ComboId comboId, ComboPattern pattern, int index) {
        boolean start = index == 0;
        boolean end = index == pattern.size() - 1;
        ComboStep step = pattern.step(index);

        if (end) byActor.remove(actorId);
        else byActor.put(actorId, new ComboState(comboId, index, pattern.size()));

        return new ComboResult.Progress(comboId, index, pattern.size(), step, start, end);
    }

    private record ComboState(ComboId comboId, int stepIndex, int stepsTotal) {
        private ComboState {
            Objects.requireNonNull(comboId);
            if (stepsTotal <= 0 || stepIndex < 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
        }
    }
}
