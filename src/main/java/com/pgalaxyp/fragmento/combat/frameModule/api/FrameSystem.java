package com.pgalaxyp.fragmento.combat.frameModule.api;

@FunctionalInterface
public interface FrameSystem {
    void tick(FrameContext frame, Object state, FrameBus bus);
}