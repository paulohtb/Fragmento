package com.pgalaxyp.fragmento.tier.server.sync;

import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.common.sync.TierStateView;
import java.util.UUID;

public final class TierStateStore {

    private final TierService service;

    public TierStateStore(TierService service) {
        this.service = service;
    }

    public TierStateView snapshot(UUID playerId) {
        var snap = service.snapshot(playerId, System.currentTimeMillis());
        return new TierStateView(snap.tier(), snap.version());
    }

    public void invalidate(UUID playerId) {
        service.invalidate(playerId);
    }
}