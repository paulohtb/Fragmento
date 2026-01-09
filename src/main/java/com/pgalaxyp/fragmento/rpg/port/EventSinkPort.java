package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import java.util.List;

public interface EventSinkPort {
    void publish(FrameContext frame, List<DomainEvent> events);
}