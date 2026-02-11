package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.LiveActorsView;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameBus;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Objects;

final class BasicAttackRuntimeSystem implements FrameSystem {
    private final BasicAttackEngine engine;

    BasicAttackRuntimeSystem(BasicAttackEngine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var live = bus.viewOpt(LiveActorsView.class).orElse(LiveActorsView.empty()).ids();
        engine.tick(frame.frameId(), live);
    }
}
