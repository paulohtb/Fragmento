package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.server.event.TierUpdatedEvent;
import java.util.UUID;
import java.util.function.Consumer;

public interface TierService {

    TierSnapshot snapshot(UUID playerId, long nowMillis);

    void registerListener(Consumer<TierUpdatedEvent> listener);
}