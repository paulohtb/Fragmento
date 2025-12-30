package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import java.util.List;
import java.util.UUID;

public interface CosmeticsService {
    List<CosmeticEntry> snapshot(UUID playerId);
    boolean equip(UUID playerId, CosmeticId cosmeticId);
    boolean unequip(UUID playerId, CosmeticSlot slot);
    void onTierChanged(UUID playerId);
    void onLogin(UUID playerId);
    void onLogout(UUID playerId);
    void onStartTracking(UUID ownerId, UUID trackerId);
}