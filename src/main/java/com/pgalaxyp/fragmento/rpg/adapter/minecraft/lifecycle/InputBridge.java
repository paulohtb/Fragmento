package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class InputBridge {

    private final TickBus bus;
    private final ActorIds actorIds;

    public InputBridge(TickBus bus, ActorIds actorIds) {
        this.bus = bus;
        this.actorIds = actorIds;
    }

    public void handle(InputIntentPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                bus.publish(new InputEvent(payload.actorId(), payload.action()))
        );
    }
}