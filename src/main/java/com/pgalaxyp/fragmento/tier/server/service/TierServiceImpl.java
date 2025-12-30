package com.pgalaxyp.fragmento.tier.server.service;

import com.pgalaxyp.fragmento.tier.common.model.Tier;
import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.common.service.TierSnapshot;
import com.pgalaxyp.fragmento.tier.common.service.TierUpdatedEvent;
import com.pgalaxyp.fragmento.tier.server.http.TierApiClient;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class TierServiceImpl implements TierService {

    private static final long TTL_MILLIS = 30L * 60L * 1000L;
    private static final long INFLIGHT_WINDOW = 15000L;

    private final TierApiClient api;
    private final ConcurrentHashMap<UUID, CacheEntry> cache = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Consumer<TierUpdatedEvent>> listeners = new CopyOnWriteArrayList<>();

    public TierServiceImpl(TierApiClient api) {
        this.api = Objects.requireNonNull(api, "api");
    }

    @Override
    public TierSnapshot snapshot(UUID playerId, long now) {
        CacheEntry e = cache.computeIfAbsent(playerId, k -> new CacheEntry());
        TierSnapshot snap = e.snapshot.get();

        if (snap == null || snap.expired(now)) {
            refresh(e, playerId, now);
            snap = e.snapshot.get();
        }

        return snap == null
                ? new TierSnapshot(Tier.inactive(), now, now + TTL_MILLIS, 0L)
                : snap;
    }

    @Override
    public void applyLocal(UUID playerId, Tier tier, long now) {
        CacheEntry e = cache.computeIfAbsent(playerId, k -> new CacheEntry());
        TierSnapshot cur = e.snapshot.get();

        if (cur != null && cur.tier().equals(tier)) {
            TierSnapshot refreshed =
                    new TierSnapshot(tier, now, now + TTL_MILLIS, cur.version());
            e.snapshot.set(refreshed);
            return;
        }

        long nextVersion = cur == null ? 1L : cur.version() + 1L;
        TierSnapshot next =
                new TierSnapshot(tier, now, now + TTL_MILLIS, nextVersion);

        e.snapshot.set(next);

        TierUpdatedEvent ev = new TierUpdatedEvent(playerId, tier, nextVersion);
        for (Consumer<TierUpdatedEvent> l : listeners) {
            l.accept(ev);
        }
    }

    @Override
    public void registerListener(Consumer<TierUpdatedEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void invalidate(UUID playerId) {
        cache.remove(playerId);
    }

    private void refresh(CacheEntry e, UUID playerId, long now) {
        long prev = e.inFlightUntil.get();
        if (prev > now) {
            return;
        }

        if (!e.inFlightUntil.compareAndSet(prev, now + INFLIGHT_WINDOW)) {
            return;
        }

        api.fetchTier(playerId).whenComplete((tier, err) -> {
            try {
                long ts = System.currentTimeMillis();
                TierSnapshot cur = e.snapshot.get();

                if (cur != null && cur.tier().equals(tier)) {
                    TierSnapshot refreshed =
                            new TierSnapshot(tier, ts, ts + TTL_MILLIS, cur.version());
                    e.snapshot.set(refreshed);
                    return;
                }

                long v = cur == null ? 1L : cur.version() + 1L;
                TierSnapshot next =
                        new TierSnapshot(tier, ts, ts + TTL_MILLIS, v);

                e.snapshot.set(next);

                TierUpdatedEvent ev = new TierUpdatedEvent(playerId, tier, v);
                for (Consumer<TierUpdatedEvent> l : listeners) {
                    l.accept(ev);
                }
            } finally {
                e.inFlightUntil.set(0L);
            }
        });
    }

    private static final class CacheEntry {
        final AtomicReference<TierSnapshot> snapshot = new AtomicReference<>();
        final AtomicLong inFlightUntil = new AtomicLong();
    }
}