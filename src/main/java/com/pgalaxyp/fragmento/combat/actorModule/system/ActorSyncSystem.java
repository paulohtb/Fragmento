package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.*;
import java.util.*;

public final class ActorSyncSystem implements FrameSystem {
    private final ActorSnapshotPort port;
    public ActorSyncSystem(ActorSnapshotPort port) { this.port = Objects.requireNonNull(port); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        List<ActorObservation> now = Objects.requireNonNull(port.snapshot());
        if (now.isEmpty()) { bus.view(ActorView.class, ActorView.empty()); return; }
        var map = new TreeMap<ActorId, ActorState>();
        for (var o : now) if (o != null) map.put(o.actorId(), new ActorState(o.classId(), o.healthHearts(), o.maxHealthHearts()));
        bus.view(ActorView.class, map.isEmpty() ? ActorView.empty() : new ActorView(map));
    }
}