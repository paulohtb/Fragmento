package com.pgalaxyp.fragmento.cosmetics.internal.access;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import java.util.UUID;

public interface PlayerTierResolver {

    CosmeticTier getTier(UUID playerId);
}