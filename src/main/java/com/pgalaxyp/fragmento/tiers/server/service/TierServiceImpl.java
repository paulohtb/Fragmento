package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierStatus;
import com.pgalaxyp.fragmento.tiers.network.TierApiClient;
import com.pgalaxyp.fragmento.tiers.server.event.TierUpdatedEvent;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TierServiceImpl implements TierService {

    private static final long SOFT_TTL_MILLIS = 30L * 60L * 1000L;
    private static final long HARD_TTL_MILLIS = 48L * 60L * 60L * 1000L;

    private final TierApiClient api;
    private final ConcurrentHashMap<UUID, TierSnapshot> cache;
    private final List<Consumer<TierUpdatedEvent>> listeners;

    public TierServiceImpl(TierApiClient api) {
        this.api = Objects.requireNonNull(api, "api");
        this.cache = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public TierSnapshot snapshot(UUID playerId, long nowMillis) {
        TierSnapshot current = cache.get(playerId);

        if (current != null && !current.expired(nowMillis)) {
            return current;
        }

        Tier fetched = api.fetchTierSync(playerId);

        if (current != null) {
            long age = nowMillis - current.fetchedAtMillis();

            if (!fetched.active() && age < HARD_TTL_MILLIS) {
                TierSnapshot kept =
                        new TierSnapshot(
                                current.tier(),
                                current.fetchedAtMillis(),
                                nowMillis + SOFT_TTL_MILLIS,
                                current.version()
                        );
                cache.put(playerId, kept);
                return kept;
            }
        }

        Tier effective = fetched;
        if (!fetched.active() && current != null && (nowMillis - current.fetchedAtMillis()) >= HARD_TTL_MILLIS) {
            effective = new Tier(current.tier().level(), TierStatus.INACTIVE);
        }

        long version = current == null ? 1L : current.version() + 1L;

        TierSnapshot next =
                new TierSnapshot(
                        effective,
                        nowMillis,
                        nowMillis + SOFT_TTL_MILLIS,
                        version
                );

        cache.put(playerId, next);

        if (current == null || !current.tier().equals(effective)) {
            TierUpdatedEvent ev = new TierUpdatedEvent(playerId, effective, version);
            for (Consumer<TierUpdatedEvent> l : listeners) {
                l.accept(ev);
            }
        }

        return next;
    }

    @Override
    public void registerListener(Consumer<TierUpdatedEvent> listener) {
        listeners.add(listener);
    }
}