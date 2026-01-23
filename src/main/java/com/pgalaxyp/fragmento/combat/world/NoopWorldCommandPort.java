package com.pgalaxyp.fragmento.combat.world;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.*;

public final class NoopWorldCommandPort implements WorldCommandPort {
    @Override
    public void apply(FrameContext frame, GameState state, List<DomainEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
    }
}