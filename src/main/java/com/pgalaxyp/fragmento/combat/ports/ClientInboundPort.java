package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.event.DomainEvent;
import com.pgalaxyp.fragmento.combat.ports.dto.GameSnapshot;
import java.util.List;

public interface ClientInboundPort {
    void acceptSnapshot(GameSnapshot snapshot);
    void acceptEvents(List<DomainEvent> events);
}