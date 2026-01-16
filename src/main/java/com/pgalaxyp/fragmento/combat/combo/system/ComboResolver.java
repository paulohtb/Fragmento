package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import java.util.*;

public final class ComboResolver {

    public sealed interface Outcome permits Outcome.Progress, Outcome.Reset, Outcome.Reject {
        record Progress(int index) implements Outcome { public Progress { if (index < 0) throw new IllegalArgumentException(); } }
        enum Reset implements Outcome { INSTANCE }
        enum Reject implements Outcome { INSTANCE }
    }

    public Outcome resolve(Optional<ComboState> stateOpt, ComboId comboId, ComboPattern pattern, ComboInput input) {
        Objects.requireNonNull(stateOpt);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(input);

        if (stateOpt.isEmpty()) return pattern.step(0).input() == input ? new Outcome.Progress(0) : Outcome.Reject.INSTANCE;

        ComboState st = stateOpt.get();

        if (!st.comboId().equals(comboId) || st.stepsTotal() != pattern.size()) {
            return pattern.step(0).input() == input ? new Outcome.Progress(0) : Outcome.Reset.INSTANCE;
        }

        int next = st.stepIndex() + 1;
        if (next < pattern.size() && pattern.step(next).input() == input) return new Outcome.Progress(next);

        if (pattern.step(0).input() == input) return new Outcome.Progress(0);

        return Outcome.Reset.INSTANCE;
    }
}
