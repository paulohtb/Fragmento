package com.pgalaxyp.fragmento.tier.server.sync;

import com.pgalaxyp.fragmento.platform.sync.VersionedStateStore;
import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.common.sync.TierStateView;
import java.util.UUID;

public final class TierStateStore implements VersionedStateStore<TierStateView> {

    private final TierService service;

    public TierStateStore(TierService service) {
        this.service = service;
    }

    @Override
    public TierStateView snapshot(UUID playerId) {
        var snap = service.snapshot(playerId, System.currentTimeMillis());
        return new TierStateView(snap.tier(), snap.version());
    }

    @Override
    public void invalidate(UUID playerId) {
        service.invalidate(playerId);
    }
}