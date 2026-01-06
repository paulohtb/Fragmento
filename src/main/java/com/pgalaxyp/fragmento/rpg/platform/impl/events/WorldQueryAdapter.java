package com.pgalaxyp.fragmento.rpg.platform.impl.events;

import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequestedEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingDataCollectedEvent;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import com.pgalaxyp.fragmento.rpg.platform.api.world.TargetingQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldTarget;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;

public final class WorldQueryAdapter implements WorldQueryGateway {

    private final WorldView world;

    public WorldQueryAdapter(WorldView world) {
        this.world = world;
    }

    @Override
    public TargetingDataCollectedEvent collect(TargetingRequestedEvent request) {
        var hit = world.raycastLivingEntity(
                new TargetingQuery(request.actorId(), request.targeting())
        );

        return new TargetingDataCollectedEvent(
                request,
                hit.map(h -> new WorldTarget(
                        h.target().position(),
                        h.target().bounds(),
                        h.target().actorIdOrZero()
                ))
        );
    }
}