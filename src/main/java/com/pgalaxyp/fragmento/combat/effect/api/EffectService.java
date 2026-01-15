package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.core.events.delta.*;
import com.pgalaxyp.fragmento.combat.core.events.event.*;
import java.util.*;

public interface EffectService {

    EffectOutcome apply(FrameContext frame, GameState state, ActionContext action, EffectIntent intent);

    record EffectOutcome(List<StateDelta> deltas, List<DomainEvent> events) {
        public EffectOutcome {
            if (deltas == null || events == null) throw new IllegalArgumentException();
            deltas = List.copyOf(deltas);
            events = List.copyOf(events);
        }

        public static EffectOutcome none() { return new EffectOutcome(List.of(), List.of()); }
    }
}