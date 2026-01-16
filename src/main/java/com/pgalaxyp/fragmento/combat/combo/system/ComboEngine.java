package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.combo.system.internal.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final ComboResolver resolver = new ComboResolver();

    @Override
    public ComboResult decide(ComboId comboId, ComboPattern pattern, ComboInput input, Optional<ComboState> previous) {
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(input);
        Objects.requireNonNull(previous);

        Optional<Integer> next = resolver.resolveNextIndex(previous, comboId, pattern, input);
        if (next.isEmpty()) return ComboResult.reject();

        int index = next.get();
        return new ComboResult.Progress(
                comboId,
                index,
                pattern.size(),
                pattern.step(index),
                index == 0,
                index == pattern.size() - 1
        );
    }

    @Override
    public ComboState start(ComboId comboId, ComboPattern pattern) {
        return new ComboState(comboId, 0, pattern.size());
    }

    @Override
    public Optional<ComboState> advanceState(ComboState state, int stepsTotal) {
        return state.stepIndex() + 1 >= stepsTotal ? Optional.empty() : Optional.of(state.advance());
    }
}