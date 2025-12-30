package com.pgalaxyp.fragmento.cosmetics.server.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedStateSyncService;
import com.pgalaxyp.fragmento.cosmetics.common.sync.CosmeticStateView;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServiceImpl;
import java.util.Objects;
import java.util.UUID;

public final class CosmeticSyncRuntime {

    private static volatile VersionedStateSyncService<CosmeticStateView> SYNC;

    private CosmeticSyncRuntime() {}

    public static void bind(CosmeticServiceImpl service) {
        Objects.requireNonNull(service, "service");

        CosmeticStateStore store = new CosmeticStateStore(service);
        CosmeticStatePublisher publisher = new CosmeticStatePublisher();

        VersionedStateSyncService<CosmeticStateView> sync =
                new VersionedStateSyncService<>(store, publisher);

        SYNC = sync;
        service.bindSync(sync);
    }

    public static void syncNow(UUID playerId) {
        VersionedStateSyncService<CosmeticStateView> sync = SYNC;
        if (sync == null || playerId == null) {
            return;
        }
        sync.syncNow(playerId);
    }

    public static void onLogout(UUID playerId) {
        VersionedStateSyncService<CosmeticStateView> sync = SYNC;
        if (sync == null || playerId == null) {
            return;
        }
        sync.onLogout(playerId);
    }
}