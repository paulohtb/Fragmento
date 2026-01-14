package com.pgalaxyp.fragmento.rpg.action.api;

import com.pgalaxyp.fragmento.rpg.action.emit.*;
import java.util.*;

public sealed interface ActionOutcome permits ActionOutcome.Accepted, ActionOutcome.Rejected, ActionOutcome.Ignored {

    record Accepted(List<ActionEmission> emissions, boolean finished) implements ActionOutcome {
        public Accepted {
            if (emissions == null) throw new IllegalArgumentException();
            emissions = List.copyOf(emissions);
        }
    }

    enum Rejected implements ActionOutcome { INSTANCE }
    enum Ignored implements ActionOutcome { INSTANCE }

    static ActionOutcome rejected() { return Rejected.INSTANCE; }
    static ActionOutcome ignored() { return Ignored.INSTANCE; }
    static ActionOutcome accepted(List<ActionEmission> emissions, boolean finished) { return new Accepted(emissions, finished); }
    static ActionOutcome acceptedNone() { return new Accepted(List.of(), false); }
}