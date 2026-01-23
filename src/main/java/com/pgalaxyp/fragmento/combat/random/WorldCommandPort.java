package com.pgalaxyp.fragmento.combat.random;

import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, List<FrameEvent> events);
}