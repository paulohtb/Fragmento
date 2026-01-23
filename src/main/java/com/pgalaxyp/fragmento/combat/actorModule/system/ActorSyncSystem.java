package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorState;
import com.pgalaxyp.fragmento.combat.actorModule.event.ActorRemoved;
import com.pgalaxyp.fragmento.combat.actorModule.event.ActorUpserted;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorObservation;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.random.*;
import java.util.*;

public final class ActorSyncSystem implements FrameSystem {
    private final ActorSnapshotPort port;

    public ActorSyncSystem(ActorSnapshotPort port) { this.port = Objects.requireNonNull(port); }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        List<ActorObservation> now = port.snapshot();
        if (now == null) now = List.of();

        Set<ActorId> live = new HashSet<>();
        for (ActorObservation o : now) {
            if (o == null) continue;
            live.add(o.actorId());
            bus.publish(new ActorUpserted(o.actorId(), ActorState.idleWithHealth(o.classId(), o.healthHearts(), o.maxHealthHearts())));
        }

        for (var e : state.actors().actors().entrySet()) {
            ActorId actorId = e.getKey();
            if (actorId != null && !live.contains(actorId)) bus.publish(new ActorRemoved(actorId));
        }
    }
}