package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record TargetingResolution(
        long actorId,
        TargetingId targetingId
) {}