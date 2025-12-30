package com.pgalaxyp.fragmento.cosmetics.server.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedStateStore;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.sync.CosmeticStateView;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import java.util.UUID;

public final class CosmeticStateStore implements VersionedStateStore<CosmeticStateView> {

    private final CosmeticService service;

    public CosmeticStateStore(CosmeticService service) {
        this.service = service;
    }

    @Override
    public CosmeticStateView snapshot(UUID playerId) {
        CosmeticLoadoutSnapshot snap = service.getSnapshot(playerId);
        return new CosmeticStateView(snap.loadout(), snap.version());
    }

    @Override
    public void invalidate(UUID playerId) {}
}