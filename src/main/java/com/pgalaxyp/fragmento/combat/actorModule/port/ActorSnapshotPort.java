package com.pgalaxyp.fragmento.combat.actorModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.LiveActorsView;

@FunctionalInterface
public interface ActorSnapshotPort {
    LiveActorsView snapshot();
}
