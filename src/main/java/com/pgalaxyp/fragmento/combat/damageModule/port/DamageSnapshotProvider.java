package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;

public interface DamageSnapshotProvider {
    DamageSnapshot snapshot(ActorStateView state, ActorId source, ActorId target);
}