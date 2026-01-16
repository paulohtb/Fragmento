package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public sealed interface ActionOutcome permits ActionOutcome.Success, ActionOutcome.Reject {
    record Success(List<EffectIntent> intents, boolean finished) implements ActionOutcome {
        public Success {
            intents = List.copyOf(Objects.requireNonNull(intents));
        }
    }

    enum Reject implements ActionOutcome { INSTANCE }

    static ActionOutcome finished(List<EffectIntent> intents) {
        return new Success(intents, true);
    }
    static ActionOutcome running(List<EffectIntent> intents) {
        return new Success(intents, false);
    }
    static ActionOutcome reject() {
        return Reject.INSTANCE;
    }
}