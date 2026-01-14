package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.core.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import java.util.List;

public interface EventSinkPort {
    void publish(FrameContext frame, List<DomainEvent> events);
}