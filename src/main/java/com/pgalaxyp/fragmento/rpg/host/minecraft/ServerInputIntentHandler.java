package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.engine.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerInputIntentHandler {

    private static ActorIds actorIds;

    public static void init(ActorIds ids) {
        actorIds = ids;
    }

    public static void handle(TickBus bus, InputIntentPayload payload, IPayloadContext ctx) {
        var player = ctx.player();
        if (player == null) return;

        var actorId = actorIds.idFor(player.getUUID());
        bus.publish(
                new ActionIntent(
                        actorId,
                        new ActionId("COMBO_ATTACK")
                )
        );
    }
}