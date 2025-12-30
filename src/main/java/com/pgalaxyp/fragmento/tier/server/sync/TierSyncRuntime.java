package com.pgalaxyp.fragmento.tier.server.sync;

import com.pgalaxyp.fragmento.platform.sync.VersionedStateSyncService;
import com.pgalaxyp.fragmento.tier.common.sync.TierStateView;
import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.common.service.TierUpdatedEvent;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class TierSyncRuntime {

    private static volatile VersionedStateSyncService<TierStateView> SYNC;

    private TierSyncRuntime() {}

    public static void bind(TierService service) {
        Objects.requireNonNull(service, "service");

        TierStateStore store = new TierStateStore(service);
        TierStatePublisher publisher = new TierStatePublisher();

        VersionedStateSyncService<TierStateView> sync =
                new VersionedStateSyncService<>(store, publisher);

        SYNC = sync;
        service.registerListener(new ForwardTierUpdates(sync));
    }

    public static void syncNow(UUID playerId) {
        VersionedStateSyncService<TierStateView> sync = SYNC;
        if (sync == null || playerId == null) {
            return;
        }
        sync.syncNow(playerId);
    }

    public static void onLogout(UUID playerId) {
        VersionedStateSyncService<TierStateView> sync = SYNC;
        if (sync == null || playerId == null) {
            return;
        }
        sync.onLogout(playerId);
    }

    private record ForwardTierUpdates(
            VersionedStateSyncService<TierStateView> sync) implements Consumer<TierUpdatedEvent> {

        @Override
            public void accept(TierUpdatedEvent ev) {
                if (ev == null) {
                    return;
                }
                sync.onUpdated(ev.playerId(), ev.version());
            }
        }
}