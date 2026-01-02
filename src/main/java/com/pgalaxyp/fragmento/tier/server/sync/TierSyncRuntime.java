package com.pgalaxyp.fragmento.tier.server.sync;

import com.pgalaxyp.fragmento.tier.common.service.TierService;
import java.util.Objects;
import java.util.UUID;

public final class TierSyncRuntime {

    private TierSyncRuntime() {}

    public static void bind(TierService service) {
        Objects.requireNonNull(service, "service");

        TierStateStore store = new TierStateStore(service);
        TierStatePublisher publisher = new TierStatePublisher();
    }

    public static void syncNow(UUID playerId) {}

    public static void onLogout(UUID playerId) {}
}