package com.pgalaxyp.fragmento.combat.damage.snapshot;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.*;

public interface DamageSnapshotProvider {
    DamageSnapshot snapshot(GameState state, ActorId source, ActorId target);
}