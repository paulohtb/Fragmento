package com.pgalaxyp.fragmento.rpg_old.client;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

public final class ClientTime {

    private long serverNowTicks;
    private long clientAtSyncNanos;

    public void sync(Time serverNow) {
        if (serverNow == null) return;
        serverNowTicks = serverNow.ticks();
        clientAtSyncNanos = System.nanoTime();
    }

    public Time now() {
        if (clientAtSyncNanos == 0L) {
            return Time.ofTicks(0L);
        }

        long nanos = System.nanoTime() - clientAtSyncNanos;
        long ticksPassed = nanos / 50000000L;
        return Time.ofTicks(serverNowTicks + ticksPassed);
    }
}