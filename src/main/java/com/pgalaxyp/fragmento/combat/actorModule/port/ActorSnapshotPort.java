package com.pgalaxyp.fragmento.combat.actorModule.port;

import java.util.List;

public interface ActorSnapshotPort {
    List<ActorObservation> snapshot();
}