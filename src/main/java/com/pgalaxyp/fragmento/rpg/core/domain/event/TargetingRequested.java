package com.pgalaxyp.fragmento.rpg.core.domain.event;

public record TargetingRequested(
        long actorId,
        long requestId,
        double maxEntityDistanceBlocks,
        double maxVirtualDistanceBlocks
) implements DomainEvent {
    public TargetingRequested {
        requestId = Math.max(0L, requestId);
        maxEntityDistanceBlocks = Math.max(0.0, maxEntityDistanceBlocks);
        maxVirtualDistanceBlocks = Math.max(0.0, maxVirtualDistanceBlocks);
    }
}