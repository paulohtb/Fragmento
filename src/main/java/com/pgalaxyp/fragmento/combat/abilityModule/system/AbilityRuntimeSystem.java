package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.Objects;

final class AbilityRuntimeSystem implements FrameSystem {
    private final AbilityEngine engine;

    AbilityRuntimeSystem(AbilityEngine engine) { this.engine = Objects.requireNonNull(engine); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var actors = bus.viewOpt(ActorView.class).orElse(ActorView.empty());
        var live = actors.ids();
        engine.tick(frame.frameId(), live);
        bus.view(AbilityViewSnapshot.class, engine.view(live, frame.frameId()));
    }
}
