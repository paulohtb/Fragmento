package com.pgalaxyp.fragmento.rpg.core.domain.action;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;

public sealed interface ActionPayload permits ActionPayload.None, ActionPayload.Combo {

    record None() implements ActionPayload {}

    record Combo(ComboSequence sequence) implements ActionPayload {
        public Combo {
            if (sequence == null) throw new IllegalArgumentException("Combo.sequence");
        }
    }

    static ActionPayload none() {
        return new None();
    }

    static ActionPayload combo(ComboSequence sequence) {
        return new Combo(sequence);
    }
}