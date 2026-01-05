package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatStateSnapshotPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class RpgNetwork {

    private static final String NETWORK_VERSION = "1";

    private RpgNetwork() {}

    public static void register(IEventBus modBus, TickBus bus) {
        modBus.addListener((RegisterPayloadHandlersEvent e) -> onRegisterPayloads(e, bus));
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event, TickBus bus) {
        var registrar = event.registrar(NETWORK_VERSION);

        registrar.playToServer(
                InputIntentPayload.TYPE,
                InputIntentPayload.STREAM_CODEC,
                (payload, ctx) -> handleInputIntent(bus, payload, ctx)
        );

        registrar.playToClient(
                CombatStateSnapshotPayload.TYPE,
                CombatStateSnapshotPayload.STREAM_CODEC,
                RpgNetwork::handleCombatSnapshot
        );
    }

    private static void handleInputIntent(TickBus bus, InputIntentPayload payload, IPayloadContext ctx) {
        if (payload == null || ctx == null) return;
        ServerInputIntentHandler.handle(bus, payload, ctx);
    }

    private static void handleCombatSnapshot(CombatStateSnapshotPayload payload, IPayloadContext ctx) {
        if (payload == null || ctx == null) return;
        ctx.enqueueWork(() -> ClientCombatStateStore.apply(payload.snapshot()));
    }
}