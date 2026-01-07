package com.pgalaxyp.fragmento.rpg.platform.api.world;

public record TargetingQuery(
        long actorId,
        long requestId,
        double maxEntityDistanceBlocks,
        double maxVirtualDistanceBlocks
) {}