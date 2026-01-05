package com.pgalaxyp.fragmento.rpg.network;

import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class InputPayloadHandler {

    private final TickBus bus;

    public InputPayloadHandler(TickBus bus) {
        this.bus = bus;
    }

    public void handle(InputIntentPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                bus.publish(new InputEvent(payload.actorId(), payload.action()))
        );
    }
}