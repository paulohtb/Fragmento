package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.combat.ports.EventSinkPort;
import java.util.List;

public final class NFEventSink implements EventSinkPort {

    @Override
    public void publish(FrameContext frame, List<DomainEvent> events) {
        if (frame == null || events == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = NFWire.encodeEvents(events);
        NFWire.sendEventsToAll(encoded);
    }
}