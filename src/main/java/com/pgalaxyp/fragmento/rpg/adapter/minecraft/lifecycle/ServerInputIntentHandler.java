package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerInputIntentHandler {

    private ServerInputIntentHandler() {}

    public static void handle(TickBus bus, InputIntentPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> bus.publish(new InputEvent(payload.actorId(), payload.action())));
    }
}