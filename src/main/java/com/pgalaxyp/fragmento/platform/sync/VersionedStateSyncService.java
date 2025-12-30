package com.pgalaxyp.fragmento.platform.sync;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VersionedStateSyncService<T extends VersionedState> {

    private final VersionedStateStore<T> store;
    private final VersionedStatePublisher<T> publisher;
    private final ConcurrentHashMap<UUID, Long> lastSent = new ConcurrentHashMap<>();

    public VersionedStateSyncService(
            VersionedStateStore<T> store,
            VersionedStatePublisher<T> publisher
    ) {
        this.store = store;
        this.publisher = publisher;
    }

    public void syncNow(UUID playerId) {
        T snap = store.snapshot(playerId);
        if (snap == null) return;

        long v = snap.version();
        Long prev = lastSent.get(playerId);
        if (prev != null && prev == v) return;

        publisher.publish(playerId, snap);
        lastSent.put(playerId, v);
    }

    public void onUpdated(UUID playerId, long version) {
        Long prev = lastSent.get(playerId);
        if (prev != null && prev == version) return;
        syncNow(playerId);
    }

    public void onLogout(UUID playerId) {
        lastSent.remove(playerId);
        store.invalidate(playerId);
    }
}