package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.event.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.*;

public final class ActorSyncSystem implements FrameSystem {
    private final ActorSnapshotPort port;

    public ActorSyncSystem(ActorSnapshotPort port) {
        this.port = Objects.requireNonNull(port);
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var prev = (ActorStateView) state;
        var now = Optional.ofNullable(port.snapshot()).orElse(List.of());
        var live = new HashSet<ActorId>(Math.max(16, now.size()));
        var classes = new HashMap<ActorId, ClassId>(Math.max(16, now.size()));
        for (var o : now) {
            if (o == null) continue;
            var id = o.actorId();
            live.add(id);
            classes.put(id, o.classId());
            var next = new ActorState(o.classId(), o.healthHearts(), o.maxHealthHearts());
            var old = prev.findActor(id).orElse(null);
            if (!next.equals(old)) bus.publish(new ActorUpserted(id, next));
        }
        for (var id : prev.actors().ids()) if (!live.contains(id)) bus.publish(new ActorRemoved(id));
        bus.view(ActorSyncView.class, new ActorSyncView(classes, live));
    }
}