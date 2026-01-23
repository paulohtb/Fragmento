package com.pgalaxyp.fragmento.combat.random;

@FunctionalInterface
public interface FrameSystem {
    void tick(FrameContext frame, GameState state, FrameBus bus);
}