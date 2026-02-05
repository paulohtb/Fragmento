package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityStartRequest;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.Objects;

final class AbilityCommandSystem implements FrameSystem {
    private final AbilityEngine engine;

    AbilityCommandSystem(AbilityEngine engine) { this.engine = Objects.requireNonNull(engine); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        for (var req : bus.events(AbilityStartRequest.class)) bus.publish(engine.tryStart(req.actorId(), req.abilityId(), frame.frameId()));
    }
}
