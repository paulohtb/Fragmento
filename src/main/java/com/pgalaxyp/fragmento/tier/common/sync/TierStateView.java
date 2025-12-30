package com.pgalaxyp.fragmento.tier.common.sync;

import com.pgalaxyp.fragmento.platform.sync.VersionedState;
import com.pgalaxyp.fragmento.tier.common.model.Tier;

public record TierStateView(
        Tier tier,
        long version
) implements VersionedState {}