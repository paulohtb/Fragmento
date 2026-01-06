package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;

public record TargetingQuery(
        long actorId,
        TargetingRequest request,
        double maxEntityDistanceBlocks,
        double maxVirtualDistanceBlocks
) {}