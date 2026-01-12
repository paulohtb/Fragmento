package com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.ports.EventSinkPort;
import java.util.List;

public final class NeoForgeEventSinkPort implements EventSinkPort {

    @Override
    public void publish(FrameContext frame, List<DomainEvent> events) {
        if (frame == null || events == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = NeoForgeNetWire.encodeEvents(events);
        NeoForgeNetWire.sendEventsToAll(encoded);
    }
}