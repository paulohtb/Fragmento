package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.client.*;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;
import com.pgalaxyp.fragmento.combat.input.platform.*;
import com.pgalaxyp.fragmento.combat.input.api.SemanticInput;
import com.pgalaxyp.fragmento.combat.content.bard.BardInputBindings;
import com.pgalaxyp.fragmento.combat.input.port.ServerIntentReceiverPort;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class FragmentoClientEvents {
    private static final ClientSnapshotReceiver RECEIVER = new ClientSnapshotReceiver();
    private static volatile ClientRuntime clientRuntime;
    private static volatile boolean joinSent;
    public static ClientSnapshotReceiver clientReceiver() { return RECEIVER; }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            joinSent = false;
            return;
        }

        ClientRuntime runtime = clientRuntime;
        if (runtime == null) clientRuntime = runtime = ClientRuntime.create();

        LocalPlayer player = mc.player;
        if (player != null && !joinSent) {
            runtime.sink.emit(new IntentEnvelope(new ActorId(player.getUUID()), new ActorJoinIntent(), null));
            joinSent = true;
        }

        if (mc.options.keyAttack.consumeClick()) {
            runtime.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION);
        }

        runtime.tickHook.onClientTick();
    }

    private record ClientRuntime(InputIntentSink sink, PrimaryActionInputHandler primaryHandler, ClientTickHook tickHook) {
        private static ClientRuntime create() {
            Object intentsObj = FragmentoMod.INTEGRATED_SERVER_INTENTS.get();
            InputIntentSink sink = intentsObj instanceof ServerIntentReceiverPort srv ? new LocalInputIntentSink(srv) : envelope -> {};
            InputSnapshotProvider snapshots = new ClientInputSnapshotProvider(RECEIVER);
            ActorInputContextProvider actorContext = new McActorContext(new ItemWeaponBinding(), () -> {
                LocalPlayer p = Minecraft.getInstance().player;
                return p == null ? java.util.Optional.empty() : java.util.Optional.of(new ActorId(p.getUUID()));
            });

            var primary = new PrimaryActionInputHandler(actorContext, snapshots, sink, new InputConsumptionPolicy(), BardInputBindings.primaryAbilityByWeapon());
            var abilityFx = new AbilityFeedbackRenderer(RECEIVER);
            var damageFx = new DamageFeedbackRenderer(RECEIVER);
            var tickHook = new ClientTickHook(abilityFx, damageFx);

            return new ClientRuntime(Objects.requireNonNull(sink), primary, tickHook);
        }
    }

    private FragmentoClientEvents() {}
}