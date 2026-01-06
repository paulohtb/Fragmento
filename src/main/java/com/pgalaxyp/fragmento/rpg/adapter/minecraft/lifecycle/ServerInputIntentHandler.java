package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputAction;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerInputIntentHandler {

    private static ActorIds actorIds;

    private ServerInputIntentHandler() {}

    public static void init(ActorIds ids) {
        actorIds = ids;
    }

    public static void handle(TickBus bus, InputIntentPayload payload, IPayloadContext ctx) {
        if (actorIds == null) return;

        var player = ctx.player();
        if (player == null) return;

        var actorId = actorIds.idFor(player.getUUID());
        var action = InputAction.valueOf(payload.action());
        bus.publish(new InputEvent(actorId, action));
    }
}