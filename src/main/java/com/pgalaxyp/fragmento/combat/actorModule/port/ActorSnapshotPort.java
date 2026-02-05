package com.pgalaxyp.fragmento.combat.actorModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;

@FunctionalInterface
public interface ActorSnapshotPort {
    ActorView snapshot();
}
