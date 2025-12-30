package com.pgalaxyp.fragmento.cosmetic.common.network;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;

record CosmeticInfoWireBase(
        CosmeticId id,
        CosmeticSlot slot,
        int requiredTier,
        int sort,
        boolean visibleToSelf,
        String modelKey
) {}