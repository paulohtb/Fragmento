package com.pgalaxyp.fragmento.tier.api;

import com.pgalaxyp.fragmento.tier.client.state.TierClientState;
import java.util.UUID;

public final class TierClientProgressionView implements PlayerProgressionView {

    @Override
    public int level(UUID playerId) {
        return TierClientState.level();
    }

    @Override
    public long version(UUID playerId) {
        return TierClientState.version();
    }

    @Override
    public String label(UUID playerId) {
        return "Tier: " + level(playerId);
    }
}