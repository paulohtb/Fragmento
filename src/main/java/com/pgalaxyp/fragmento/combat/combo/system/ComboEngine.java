package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import java.util.*;

public final class ComboEngine implements ComboService {
    private final ComboResolver resolver = new ComboResolver();

    @Override
    public ComboResult decide(ComboId comboId, ComboPattern pattern, ComboInput input, Optional<ComboState> previous) {
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(input);
        Objects.requireNonNull(previous);

        var o = resolver.resolve(previous, comboId, pattern, input);
        return switch (o) {
            case ComboResolver.Outcome.Progress p -> {
                int index = p.index();
                boolean start = index == 0;
                boolean end = index == pattern.size() - 1;
                ComboStep step = pattern.step(index);
                yield new ComboResult.Progress(comboId, index, pattern.size(), step, start, end);
            }
            case ComboResolver.Outcome.Reset __ -> ComboResult.reset(comboId);
            case ComboResolver.Outcome.Reject __ -> ComboResult.reject();
        };
    }
}
