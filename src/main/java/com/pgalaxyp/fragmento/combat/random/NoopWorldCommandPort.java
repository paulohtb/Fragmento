package com.pgalaxyp.fragmento.combat.random;

import java.util.*;

public final class NoopWorldCommandPort implements WorldCommandPort {
    @Override
    public void apply(FrameContext frame, GameState state, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
    }
}