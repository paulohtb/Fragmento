package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.client.AbilityFeedbackRenderer;
import com.pgalaxyp.fragmento.combat.client.ClientInputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.client.ClientSnapshotReceiver;
import com.pgalaxyp.fragmento.combat.client.ClientTickHook;
import com.pgalaxyp.fragmento.combat.client.DamageFeedbackRenderer;
import com.pgalaxyp.fragmento.combat.content.FragmentoDomainContent;
import com.pgalaxyp.fragmento.combat.content.minecraft.FragmentoMinecraftContent;
import com.pgalaxyp.fragmento.combat.input.api.InputDecision;
import com.pgalaxyp.fragmento.combat.input.api.SemanticInput;
import com.pgalaxyp.fragmento.combat.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.input.platform.LocalInputIntentSink;
import com.pgalaxyp.fragmento.combat.input.platform.McActorContext;
import com.pgalaxyp.fragmento.combat.input.port.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.combat.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.combat.input.system.PrimaryActionInputHandler;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class FragmentoClientEvents {

    private static final ClientSnapshotReceiver RECEIVER = new ClientSnapshotReceiver();
    private static volatile ClientRuntime runtime;
    private static volatile boolean joinSent;

    public static ClientSnapshotReceiver clientReceiver() {
        return RECEIVER;
    }

    @SubscribeEvent
    public static void onKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            joinSent = false;
            return;
        }

        ClientRuntime r = runtime;
        if (r == null) runtime = r = ClientRuntime.create();

        r.refreshSink();

        KeyMapping attack = mc.options.keyAttack;
        if (event.getKeyMapping() == attack) {
            InputDecision d = r.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION);
            if (d.consumeVanilla()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            joinSent = false;
            return;
        }

        ClientRuntime r = runtime;
        if (r == null) runtime = r = ClientRuntime.create();

        r.refreshSink();

        LocalPlayer player = mc.player;
        if (player != null && !joinSent && r.sinkHolder.isActive()) {
            r.sinkHolder.emit(
                    new IntentEnvelope(new ActorId(player.getUUID()), new ActorJoinIntent(), null)
            );
            joinSent = true;
        }

        r.tickHook.onClientTick();
    }

    private record ClientRuntime(
            SinkHolder sinkHolder,
            PrimaryActionInputHandler primaryHandler,
            ClientTickHook tickHook
    ) {
        void refreshSink() {
            sinkHolder.refresh();
        }

        static ClientRuntime create() {
            var sinkHolder = new SinkHolder();

            InputSnapshotProvider snapshots = new ClientInputSnapshotProvider(RECEIVER);
            var weaponBinding = FragmentoMinecraftContent.REGISTRY.clientWeaponBinding();

            ActorInputContextProvider actorContext = new McActorContext(
                    weaponBinding,
                    () -> {
                        LocalPlayer p = Minecraft.getInstance().player;
                        return p == null
                                ? Optional.empty()
                                : Optional.of(new ActorId(p.getUUID()));
                    }
            );

            var primary = new PrimaryActionInputHandler(
                    actorContext,
                    snapshots,
                    sinkHolder,
                    new InputConsumptionPolicy(),
                    FragmentoDomainContent.CATALOG.primaryBindings()
            );

            var abilityFx = new AbilityFeedbackRenderer(RECEIVER);
            var damageFx = new DamageFeedbackRenderer(RECEIVER);
            var tickHook = new ClientTickHook(abilityFx, damageFx);

            return new ClientRuntime(sinkHolder, primary, tickHook);
        }
    }

    private static final class SinkHolder implements InputIntentSink {
        private volatile InputIntentSink delegate = envelope -> {};
        private volatile boolean active;

        void refresh() {
            Object intentsObj = FragmentoMod.INTEGRATED_SERVER_INTENTS.get();
            if (intentsObj instanceof ServerIntentReceiverPort srv) {
                delegate = new LocalInputIntentSink(srv);
                active = true;
            } else {
                delegate = envelope -> {};
                active = false;
            }
        }

        boolean isActive() {
            return active;
        }

        @Override
        public void emit(IntentEnvelope envelope) {
            delegate.emit(Objects.requireNonNull(envelope));
        }
    }

    private FragmentoClientEvents() {}
}