package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import java.util.UUID;

public interface CosmeticService {

    CosmeticLoadoutSnapshot getSnapshot(UUID playerId);

    boolean equipBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId);

    boolean unequipBase(UUID playerId, CosmeticSlot slot);

    void setForced(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId);

    void clearForced(UUID playerId, CosmeticSlot slot);
}