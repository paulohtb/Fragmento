package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.bridge.sync.VersionedStateStore;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import com.pgalaxyp.fragmento.tiers.common.sync.TierStateView;
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