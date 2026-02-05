package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.Objects;

public record ActorSnapshotSystem(ActorSnapshotPort port) implements FrameSystem {
    public ActorSnapshotSystem { Objects.requireNonNull(port); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        bus.view(ActorView.class, Objects.requireNonNull(port.snapshot()));
    }
}
