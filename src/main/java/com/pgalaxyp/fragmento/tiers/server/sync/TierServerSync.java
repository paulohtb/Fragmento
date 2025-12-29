package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.tiers.common.network.TierLevelSyncPacket;
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

    private TierServerSync() {}

    public static void bindListenerOnce(TierService service) {
        if (service == null || listenerBound) {
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

        UUID id = sp.getUUID();
        TierService service = TierServices.service();

        service.invalidate(id);

        long now = System.currentTimeMillis();
        TierSnapshot snap = service.snapshot(id, now);

        long v = snap.version();
        Long prev = LAST_SENT.get(id);
        if (prev == null || prev != v) {
            int level = snap.tier().level().value();
            PacketDistributor.sendToPlayer(
                    sp,
                    new TierLevelSyncPacket(level, v)
            );
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
            if (prev != null && prev == v) {
                return;
            }

            PUBLISHER.publish(id, ev.tier(), v);
            LAST_SENT.put(id, v);
        }
    }
}