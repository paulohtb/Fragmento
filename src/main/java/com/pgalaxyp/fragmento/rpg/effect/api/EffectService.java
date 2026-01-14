package com.pgalaxyp.fragmento.rpg.effect.api;

import com.pgalaxyp.fragmento.rpg.core.time.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.action.api.*;
import com.pgalaxyp.fragmento.rpg.effect.model.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.core.events.delta.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import java.util.*;

public interface EffectService {

    EffectOutcome apply(FrameContext frame, GameContent content, GameState state, ActionContext actionContext, EffectIntent intent, int queryIndexBase, int eventIndexBase);

    record EffectOutcome(List<StateDelta> deltas, List<DomainEvent> events, int queriesUsed, int eventsUsed) {
        public EffectOutcome {
            Objects.requireNonNull(deltas, "deltas cannot be null");
            Objects.requireNonNull(events, "events cannot be null");
            if (queriesUsed < 0) throw new IllegalArgumentException("queriesUsed must be zero or positive");
            if (eventsUsed < 0) throw new IllegalArgumentException("eventsUsed must be zero or positive");
            deltas = List.copyOf(deltas);
            events = List.copyOf(events);
        }

        public static EffectOutcome none() {
            return new EffectOutcome(List.of(), List.of(), 0, 0);
        }
    }
}