package com.pgalaxyp.fragmento.combat.flow;

import com.pgalaxyp.fragmento.combat.core.state.GameState;

@FunctionalInterface
public interface FrameSystem {
    void tick(FrameContext frame, GameState state, FrameBus bus);
}