package com.pgalaxyp.fragmento.combat.actorModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import java.util.List;

@FunctionalInterface
public interface ActorHealthPort {
    ActorView apply(ActorView base, List<HealthDelta> deltas);
}