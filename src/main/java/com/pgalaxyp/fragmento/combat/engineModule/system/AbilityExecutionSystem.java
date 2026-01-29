package com.pgalaxyp.fragmento.combat.engineModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.Objects;

public record AbilityExecutionSystem(FrameSystem delegate) implements FrameSystem {
    public AbilityExecutionSystem { Objects.requireNonNull(delegate); }
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) { delegate.tick(frame, state, bus); }
}