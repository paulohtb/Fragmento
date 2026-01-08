package com.pgalaxyp.fragmento.rpg.core.port;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record TargetingResolved(
        long actorId,
        TargetingId targetingId
) {}