package com.pgalaxyp.fragmento.cosmetics.server.validation;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import java.util.UUID;

public interface PlayerTierResolver {

    Tier resolve(UUID playerId);
}