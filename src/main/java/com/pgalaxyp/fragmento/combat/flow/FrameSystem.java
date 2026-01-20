package com.pgalaxyp.fragmento.combat.flow;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;

@FunctionalInterface
public interface FrameSystem {
    void tick(FrameContext frame, GameState state, FrameBus bus);
}