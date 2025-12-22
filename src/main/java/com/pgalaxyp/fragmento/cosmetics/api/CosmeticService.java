package com.pgalaxyp.fragmento.cosmetics.api;

import java.util.UUID;

public interface CosmeticService {

    CosmeticLoadout getEffective(UUID playerId);

    CosmeticValidationResult setBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId);

    void clearBase(UUID playerId, CosmeticSlot slot);

    void setForced(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId);

    void clearForced(UUID playerId, CosmeticSlot slot);

    void clearAllForced(UUID playerId);
}