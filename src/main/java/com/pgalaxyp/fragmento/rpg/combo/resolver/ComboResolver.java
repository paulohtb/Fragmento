package com.pgalaxyp.fragmento.rpg.combo.resolver;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.combo.state.*;
import com.pgalaxyp.fragmento.rpg.combo.model.*;
import java.util.*;

public final class ComboResolver {

    public Optional<Integer> nextStepIndex(Optional<ComboState> stateOpt, ComboDefinition def, ComboInput input) {
        if (stateOpt == null || def == null || input == null) {
            throw new IllegalArgumentException();
        }

        int total = def.stepsTotal();
        if (stateOpt.isEmpty()) {
            ComboStep first = def.pattern().step(0);
            if (first.input() != input) {
                return Optional.empty();
            }
            return Optional.of(0);
        }

        ComboState st = stateOpt.get();
        if (!st.actionKey().equals(def.actionKey())) {
            return Optional.empty();
        }
        if (st.stepsTotal() != total) {
            return Optional.empty();
        }

        int next = st.stepIndex() + 1;
        if (next >= total) {
            return Optional.empty();
        }

        ComboStep step = def.pattern().step(next);
        if (step.input() != input) {
            return Optional.empty();
        }

        return Optional.of(next);
    }

    public ComboPattern validatePattern(ComboPattern p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return p;
    }
}