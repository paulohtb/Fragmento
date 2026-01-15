package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.event.DomainEvent;
import java.util.List;

public interface EventSinkPort {
    void publish(FrameContext frame, List<DomainEvent> events);
}