package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import java.util.UUID;

public interface CosmeticSyncPublisher {

    void publish(UUID playerId, CosmeticLoadoutSnapshot snapshot);
}