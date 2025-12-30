package com.pgalaxyp.fragmento.tiers.common.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedState;
import com.pgalaxyp.fragmento.tiers.common.model.Tier;

public record TierStateView(
        Tier tier,
        long version
) implements VersionedState {}