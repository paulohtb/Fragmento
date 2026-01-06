package com.pgalaxyp.fragmento.rpg.core.domain.zone;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;

public record SpawnQuery(
        long actorId,
        Target target,
        SpawnRule rule,
        SpawnSide lastSide,
        double distance
) {}