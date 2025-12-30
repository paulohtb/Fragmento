package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;

record CosmeticInfoWireBase(
        CosmeticId id,
        CosmeticSlot slot,
        int requiredTier,
        int sort,
        boolean visibleToSelf,
        String modelKey
) {}