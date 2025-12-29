package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import java.util.UUID;

public interface CosmeticSyncPublisher {
    void publish(UUID playerId, CosmeticLoadoutSnapshot snapshot);
}