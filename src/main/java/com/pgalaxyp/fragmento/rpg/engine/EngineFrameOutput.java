package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.List;

public record EngineFrameOutput(
        FrameContext frame,
        GameSnapshot snapshot,
        List<DomainEvent> events
) {
    public EngineFrameOutput {
        if (frame == null || snapshot == null || events == null) {
            throw new IllegalArgumentException();
        }
        events = List.copyOf(events);
    }
}