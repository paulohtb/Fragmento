package com.pgalaxyp.fragmento.rpg.platform.minecraft.net;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime.FragmentoClientRuntime;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime.FragmentoServerRuntime;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.List;

public final class FragmentoNet {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar r = event.registrar("1");
        r.playToServer(C2SIntentPayload.TYPE, C2SIntentPayload.STREAM_CODEC, FragmentoNet::handleIntent);
        r.playToClient(S2CSnapshotPayload.TYPE, S2CSnapshotPayload.STREAM_CODEC, FragmentoNet::handleSnapshot);
        r.playToClient(S2CEventsPayload.TYPE, S2CEventsPayload.STREAM_CODEC, FragmentoNet::handleEvents);
    }

    public static void sendIntentToServer(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        int kind;
        String action = "";
        if (env.intent() instanceof ComboStartIntent) {
            kind = 0;
        } else if (env.intent() instanceof ComboAdvanceIntent(ActionId actionId)) {
            kind = 1;
            action = actionId.value();
        } else {
            throw new IllegalArgumentException();
        }
        byte[] bytes = FragmentoNetBytes.encodeIntent(env.actorId().value(), kind, action);
        PacketDistributor.sendToServer(new C2SIntentPayload(bytes));
    }

    public static void sendSnapshotToAll(byte[] encoded) {
        PacketDistributor.sendToAllPlayers(new S2CSnapshotPayload(encoded));
    }

    public static void sendEventsToAll(byte[] encoded) {
        PacketDistributor.sendToAllPlayers(new S2CEventsPayload(encoded));
    }

    private static void handleIntent(C2SIntentPayload payload, IPayloadContext context) {
        FragmentoNetBytes.DecodedIntent decoded = FragmentoNetBytes.decodeIntent(payload.data());
        ActorId actorId = new ActorId(decoded.actorIdValue());
        DomainIntent intent;
        if (decoded.kind() == 0) {
            intent = new ComboStartIntent();
        } else if (decoded.kind() == 1) {
            intent = new ComboAdvanceIntent(new ActionId(decoded.actionIdValue()));
        } else {
            return;
        }
        FragmentoServerRuntime rt = FragmentoServerRuntime.get(context);
        if (rt == null) {
            return;
        }
        rt.enqueue(IntentEnvelope.of(actorId, intent));
    }

    private static void handleSnapshot(S2CSnapshotPayload payload, IPayloadContext context) {
        FragmentoClientRuntime.acceptSnapshotBytes(payload.data());
    }

    private static void handleEvents(S2CEventsPayload payload, IPayloadContext context) {
        List<?> e = FragmentoNetBytes.decodeEvents(payload.data());
        FragmentoClientRuntime.acceptEvents(e);
    }

    private FragmentoNet() {}
}