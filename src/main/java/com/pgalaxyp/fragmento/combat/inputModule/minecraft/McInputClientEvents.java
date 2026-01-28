package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.mod.FragmentoMod;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.inputModule.system.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.inputModule.port.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import java.util.*;
import java.util.function.Supplier;
import net.minecraft.client.*;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class McInputClientEvents {
    private static final ClientSnapshotReceiver RECEIVER = new ClientSnapshotReceiver();
    private static volatile Runtime runtime;

    public static ClientSnapshotReceiver clientReceiver() { return RECEIVER; }

    @SubscribeEvent
    public static void onKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft instance = Minecraft.getInstance();
        if (instance.level == null) return;

        KeyMapping attack = instance.options.keyAttack;
        if (event.getKeyMapping() != attack) return;

        Runtime r = runtime;
        if (r == null) runtime = r = Runtime.create();

        InputDecision decision = r.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION);
        if (decision.consumeVanilla()) event.setCanceled(true);
    }

    private record Runtime(PrimaryActionInputHandler primaryHandler) {
        static Runtime create() {
            InputSnapshotProvider snapshots = new ClientInputSnapshotProvider(RECEIVER);
            var weaponBinding = FragmentoMinecraftContent.REGISTRY.clientWeaponBinding();

            LocalActorProvider localProvider = () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                return player == null ? Optional.empty() : Optional.of(new ActorId(player.getUUID()));
            };

            ActorInputContextProvider actorContext = new McActorContext(weaponBinding, localProvider);
            InputIntentSink sink = new IntegratedServerIntentSink(FragmentoMod.INTEGRATED_SERVER_INTENTS::get);
            var primary = new PrimaryActionInputHandler(actorContext, snapshots, sink, new InputConsumptionPolicy());

            return new Runtime(primary);
        }
    }

    private static final class IntegratedServerIntentSink implements InputIntentSink {
        private static final InputIntentSink NOOP = envelope -> {};
        private final Supplier<Object> lookup;
        private volatile Object last;
        private volatile InputIntentSink delegate = NOOP;

        IntegratedServerIntentSink(Supplier<Object> lookup) { this.lookup = Objects.requireNonNull(lookup); }

        @Override
        public void emit(IntentEnvelope envelope) {
            Object object = lookup.get();
            if (object != last) {
                last = object;
                delegate = object instanceof ServerIntentReceiverPort intent ? new LocalInputIntentSink(intent) : NOOP;
            }
            delegate.emit(Objects.requireNonNull(envelope));
        }
    }

    private McInputClientEvents() {}
}