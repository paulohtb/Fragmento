package com.pgalaxyp.fragmento.rpg.platform.minecraft.port;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNet;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNetBytes;
import com.pgalaxyp.fragmento.rpg.port.EventSinkPort;
import java.util.List;

public final class MinecraftEventSinkPortNeoForge implements EventSinkPort {

    @Override
    public void publish(FrameContext frame, List<DomainEvent> events) {
        if (frame == null || events == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = FragmentoNetBytes.encodeEvents(events);
        FragmentoNet.sendEventsToAll(encoded);
    }
}