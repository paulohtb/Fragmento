package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;
import java.util.*;

record ActorHealthEngine() implements ActorHealthPort {
    @Override public ActorView apply(ActorView base, List<HealthDelta> deltas) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(deltas);
        if (deltas.isEmpty()) return base;
        var map = new TreeMap<>(base.actors());
        boolean changed = false;
        for (var d : deltas) {
            if (d == null) throw new IllegalArgumentException();
            var s = map.get(d.actorId());
            if (s == null) continue;
            int max = s.maxHealthHearts();
            int nh = Math.max(0, Math.min(max, s.healthHearts() + d.deltaHearts()));
            if (nh != s.healthHearts()) { map.put(d.actorId(), new ActorState(s.classId(), nh, max)); changed = true; }
        }
        return changed ? new ActorView(map) : base;
    }
}