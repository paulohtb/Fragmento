package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.event.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import java.util.*;

public final class ActorSyncSystem implements FrameSystem {
    private final ActorSnapshotPort port;

    public ActorSyncSystem(ActorSnapshotPort port) {
        this.port = Objects.requireNonNull(port);
    }

    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var gs = (GameState) state;
        var now = Optional.ofNullable(port.snapshot()).orElseGet(List::of);
        var live = new HashSet<ActorId>(Math.max(16, now.size()));
        for (var o : now) {
            if (o == null) continue;
            live.add(o.actorId());
            bus.publish(new ActorUpserted(o.actorId(), ActorState.idleWithHealth(o.classId(), o.healthHearts(), o.maxHealthHearts())));
        }
        for (var actorId : gs.actors().actors().navigableKeySet()) {
            if (actorId != null && !live.contains(actorId)) bus.publish(new ActorRemoved(actorId));
        }
    }
}