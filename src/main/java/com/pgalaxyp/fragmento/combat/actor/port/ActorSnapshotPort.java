package com.pgalaxyp.fragmento.combat.actor.port;

import java.util.List;

public interface ActorSnapshotPort {
    List<ActorObservation> snapshot();
}