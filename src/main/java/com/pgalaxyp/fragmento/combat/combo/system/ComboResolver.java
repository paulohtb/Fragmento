package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import java.util.*;

public final class ComboResolver {

    public Optional<Integer> nextStepIndex(Optional<ComboState> stateOpt, ComboId comboId, ComboPattern pattern, ComboInput input) {
        if (stateOpt == null || comboId == null || pattern == null || input == null) throw new IllegalArgumentException();

        int total = pattern.size();
        if (stateOpt.isEmpty()) {
            return pattern.step(0).input() == input ? Optional.of(0) : Optional.empty();
        }

        ComboState st = stateOpt.get();
        if (!st.comboId().equals(comboId) || st.stepsTotal() != total) return Optional.empty();

        int next = st.stepIndex() + 1;
        if (next >= total) return Optional.empty();
        return pattern.step(next).input() == input ? Optional.of(next) : Optional.empty();
    }
}