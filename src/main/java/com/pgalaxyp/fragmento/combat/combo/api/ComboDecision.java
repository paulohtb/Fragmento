package com.pgalaxyp.fragmento.combat.combo.api;

public sealed interface ComboDecision permits ComboDecision.Proposed, ComboDecision.Reset, ComboDecision.Reject {

    record Proposed(
            ComboId comboId,
            int stepIndex,
            int stepsTotal,
            ComboStep step,
            boolean start,
            boolean end,
            Runnable commit
    ) implements ComboDecision {
        public Proposed {
            if (comboId == null || step == null || commit == null) throw new IllegalArgumentException();
            if (stepIndex < 0 || stepsTotal <= 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
            if (step.index() != stepIndex) throw new IllegalArgumentException();
            if (start && stepIndex != 0) throw new IllegalArgumentException();
            if (end && stepIndex != stepsTotal - 1) throw new IllegalArgumentException();
        }
    }

    record Reset(ComboId comboId, Runnable commit) implements ComboDecision {
        public Reset { if (comboId == null || commit == null) throw new IllegalArgumentException(); }
    }

    record Reject() implements ComboDecision {}

    static ComboDecision reject() { return new Reject(); }
    static ComboDecision reset(ComboId comboId, Runnable commit) { return new Reset(comboId, commit); }
}