package com.pgalaxyp.fragmento.cosmetic.server.service;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;
import java.util.List;
import java.util.UUID;

public interface CosmeticsPublisher {
    void sendFull(UUID ownerId, UUID recipientId, int catalogVersion, long rosterVersion, List<CosmeticEntry> entries);
    void sendDelta(UUID ownerId, long rosterVersion, CosmeticEntry entry, boolean alsoTrackers);
}