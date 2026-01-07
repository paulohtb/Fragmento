package com.pgalaxyp.fragmento.rpg.platform.api.events;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;

public interface WorldQueryGateway {
    void handle(DomainEvent event);
}