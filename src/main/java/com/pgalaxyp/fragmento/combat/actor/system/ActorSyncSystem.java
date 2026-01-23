package com.pgalaxyp.fragmento.combat.actor.system;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.actor.api.ActorState;
import com.pgalaxyp.fragmento.combat.actor.event.ActorRemoved;
import com.pgalaxyp.fragmento.combat.actor.event.ActorUpserted;
import com.pgalaxyp.fragmento.combat.actor.port.ActorObservation;
import com.pgalaxyp.fragmento.combat.actor.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ActorSyncSystem implements FrameSystem {

    private final ActorSnapshotPort port;

    public ActorSyncSystem(ActorSnapshotPort port) {
        this.port = Objects.requireNonNull(port);
    }

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

            ActorState next = ActorState.idleWithHealth(o.classId(), o.healthHearts(), o.maxHealthHearts());
            bus.publish(new ActorUpserted(o.actorId(), next));
        }

        for (var entry : state.actors().entrySet()) {
            if (entry == null) continue;
            var actorId = entry.getKey();
            if (actorId == null) continue;
            if (!live.contains(actorId)) {
                bus.publish(new ActorRemoved(actorId));
            }
        }
    }
}