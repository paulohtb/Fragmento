package com.pgalaxyp.fragmento.rpg.gameplay.time;

import com.pgalaxyp.fragmento.rpg.platform.api.time.PlatformTime;
import java.util.Objects;

public final class TimeSource {
    private final PlatformTime platformTime;
    private long lastNow;

    public TimeSource(PlatformTime platformTime) {
        this.platformTime = Objects.requireNonNull(platformTime);
        this.lastNow = platformTime.nowNanos();
    }

    public GameTime sample() {
        var now = platformTime.nowNanos();
        var delta = Math.max(0L, now - lastNow);
        lastNow = now;
        return new GameTime(now, delta);
    }
}