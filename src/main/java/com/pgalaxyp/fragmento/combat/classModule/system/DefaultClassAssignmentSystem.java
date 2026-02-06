package com.pgalaxyp.fragmento.combat.classModule.system;

import com.pgalaxyp.fragmento.combat.classModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.LiveActorsView;
import java.util.*;

public final class DefaultClassAssignmentSystem implements FrameSystem {
    private final ClassId defaultClassId;
    private final Map<com.pgalaxyp.fragmento.combat.actorModule.api.ActorId, ClassId> byActor = new HashMap<>();
    private Set<com.pgalaxyp.fragmento.combat.actorModule.api.ActorId> lastLive = Set.of();

    public DefaultClassAssignmentSystem(ClassId defaultClassId) {
        this.defaultClassId = Objects.requireNonNull(defaultClassId);
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var live = bus.viewOpt(LiveActorsView.class).orElse(LiveActorsView.empty()).ids();
        if (!live.equals(lastLive)) {
            lastLive = live;
            byActor.keySet().retainAll(live);
            for (var id : live) byActor.putIfAbsent(id, defaultClassId);
        }
        bus.view(ActorClassView.class, byActor.isEmpty() ? ActorClassView.empty() : new ActorClassView(byActor));
    }
}