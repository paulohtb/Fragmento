package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;

public interface SpawnDefaultsProvider {
    SpawnDefaults defaultsFor(ActorId actorId);
}