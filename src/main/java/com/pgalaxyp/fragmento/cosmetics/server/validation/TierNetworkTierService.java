package com.pgalaxyp.fragmento.cosmetics.server.validation;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.server.event.TierUpdatedEvent;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import com.pgalaxyp.fragmento.tiers.server.service.TierSnapshot;
import com.pgalaxyp.fragmento.tiers.network.TierNetwork;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TierNetworkTierService implements TierService {

    private final List<Consumer<TierUpdatedEvent>> listeners;
    private volatile TierService delegate;

    public TierNetworkTierService() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public TierSnapshot snapshot(UUID playerId, long nowMillis) {
        TierService s = resolve();
        if (s == null) {
            Tier t = Tier.unknown();
            return new TierSnapshot(t, nowMillis, nowMillis, 0L);
        }
        return s.snapshot(playerId, nowMillis);
    }

    @Override
    public void registerListener(Consumer<TierUpdatedEvent> listener) {
        Objects.requireNonNull(listener, "listener");
        listeners.add(listener);
        TierService s = resolve();
        if (s != null) {
            s.registerListener(listener);
        }
    }

    @Override
    public void invalidate(UUID playerId) {
        TierService s = resolve();
        if (s != null) {
            s.invalidate(playerId);
        }
    }

    private TierService resolve() {
        TierService current = delegate;
        TierService resolved = TierNetwork.service();
        if (resolved == null) {
            return current;
        }
        if (current == resolved) {
            return current;
        }
        delegate = resolved;
        for (Consumer<TierUpdatedEvent> l : listeners) {
            resolved.registerListener(l);
        }
        return resolved;
    }
}