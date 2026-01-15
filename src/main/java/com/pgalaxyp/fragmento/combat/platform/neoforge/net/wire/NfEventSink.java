package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.event.DomainEvent;
import com.pgalaxyp.fragmento.combat.ports.EventSinkPort;
import java.util.List;

public final class NfEventSink implements EventSinkPort {

    @Override
    public void publish(FrameContext frame, List<DomainEvent> events) {
        if (frame == null || events == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = NfWire.encodeEvents(events);
        NfWire.sendEventsToAll(encoded);
    }
}