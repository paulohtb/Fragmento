package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.action.emit.*;
import java.util.*;

public sealed interface ActionOutcome permits ActionOutcome.Success, ActionOutcome.Reject {
    record Success(List<ActionEmission> emissions, boolean finished) implements ActionOutcome {
        public Success { emissions = List.copyOf(Objects.requireNonNull(emissions)); }
    }

    enum Reject implements ActionOutcome { INSTANCE }

    static ActionOutcome finished(List<ActionEmission> emissions) { return new Success(emissions, true); }
    static ActionOutcome running(List<ActionEmission> emissions) { return new Success(emissions, false); }
    static ActionOutcome reject() { return Reject.INSTANCE; }
}