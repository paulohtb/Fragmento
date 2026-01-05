package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.platform.api.time.PlatformTime;

public final class MinecraftPlatformTime implements PlatformTime {
    @Override
    public long nowNanos() {
        return System.nanoTime();
    }
}