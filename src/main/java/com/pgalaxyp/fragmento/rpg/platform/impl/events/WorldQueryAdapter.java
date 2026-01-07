package com.pgalaxyp.fragmento.rpg.platform.impl.events;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;

public final class WorldQueryAdapter implements WorldQueryGateway {

    private final WorldView world;

    public WorldQueryAdapter(WorldView world) {
        this.world = world;
    }

    @Override
    public void handle(DomainEvent event) {}
}