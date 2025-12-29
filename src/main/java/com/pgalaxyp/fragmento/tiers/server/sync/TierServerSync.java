package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.tiers.common.network.TierSyncPacket;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import com.pgalaxyp.fragmento.tiers.common.service.TierSnapshot;
import com.pgalaxyp.fragmento.tiers.common.service.TierUpdatedEvent;
import com.pgalaxyp.fragmento.tiers.server.service.TierServices;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento")
public final class TierServerSync {

    private static final ConcurrentHashMap<UUID, Long> LAST_SENT = new ConcurrentHashMap<>();
    private static final TierSyncPublisher PUBLISHER = new TierSyncPublisher();

    private static volatile boolean listenerBound;

    private TierServerSync() {
    }

    public static void bindListenerOnce(TierService service) {
        if (service == null) {
            return;
        }
        if (listenerBound) {
            return;
        }
        listenerBound = true;
        service.registerListener(new PublishOnChange());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }

        TierService service = TierServices.service();
        UUID id = sp.getUUID();

        service.invalidate(id);

        long now = System.currentTimeMillis();
        TierSnapshot snap = service.snapshot(id, now);

        long v = snap.version();
        Long prev = LAST_SENT.get(id);
        long pv = prev == null ? Long.MIN_VALUE : prev;
        if (v != pv) {
            PacketDistributor.sendToPlayer(sp, new TierSyncPacket(snap.tier(), v));
            LAST_SENT.put(id, v);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }
        UUID id = sp.getUUID();
        LAST_SENT.remove(id);
        TierServices.service().invalidate(id);
    }

    private static final class PublishOnChange implements Consumer<TierUpdatedEvent> {

        @Override
        public void accept(TierUpdatedEvent ev) {
            if (ev == null) {
                return;
            }

            UUID id = ev.playerId();
            long v = ev.version();

            Long prev = LAST_SENT.get(id);
            long pv = prev == null ? Long.MIN_VALUE : prev;
            if (v == pv) {
                return;
            }

            PUBLISHER.publish(id, new TierSnapshot(ev.tier(), System.currentTimeMillis(), System.currentTimeMillis(), v));
            LAST_SENT.put(id, v);
        }
    }
}