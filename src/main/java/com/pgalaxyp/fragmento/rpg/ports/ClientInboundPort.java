package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.List;

public interface ClientInboundPort {
    void acceptSnapshot(GameSnapshot snapshot);
    void acceptEvents(List<DomainEvent> events);
}