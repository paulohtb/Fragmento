package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.combo.model.*;

public sealed interface ComboResult permits ComboResult.Progress, ComboResult.Reject {
    record Progress(ComboId comboId, int stepIndex, int stepsTotal, ComboStep step, boolean start, boolean end) implements ComboResult {
        public Progress {
            if (comboId == null || step == null) throw new IllegalArgumentException();
            if (stepIndex < 0 || stepsTotal <= 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
            if (step.index() != stepIndex) throw new IllegalArgumentException();
            if (start && stepIndex != 0) throw new IllegalArgumentException();
            if (end && stepIndex != stepsTotal - 1) throw new IllegalArgumentException();
        }
    }

    record Reject() implements ComboResult {}
    static ComboResult reject() { return new Reject(); }
}