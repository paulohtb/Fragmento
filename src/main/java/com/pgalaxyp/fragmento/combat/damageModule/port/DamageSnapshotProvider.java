package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;

public interface DamageSnapshotProvider {
    DamageSnapshot snapshot(GameState state, ActorId source, ActorId target);
}