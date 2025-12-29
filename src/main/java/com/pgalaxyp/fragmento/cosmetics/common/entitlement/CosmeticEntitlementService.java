package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import java.util.UUID;

public interface CosmeticEntitlementService {

    boolean allowed(UUID playerId, CosmeticDefinition def);

    long version(UUID playerId);

    String label(UUID playerId);
}