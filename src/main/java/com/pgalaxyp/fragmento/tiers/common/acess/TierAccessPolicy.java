package com.pgalaxyp.fragmento.tiers.common.acess;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;

public interface TierAccessPolicy {
    boolean allowed(Tier tier, TierLevel required);
}