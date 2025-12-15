package com.pgalaxyp.fragmento.content.bard.entity;

import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public final class WindVortexLimitService {

    public enum VortexTier {
        MINOR,
        MEDIUM
    }

    private WindVortexLimitService() {
    }

    public static boolean canSpawn(ServerLevel level, UUID ownerId, VortexTier tier) {
        if (level == null || ownerId == null || tier == null) return false;
        long now = level.getGameTime();
        return WindVortexLimitSavedData.get(level).canSpawn(ownerId, tier, now);
    }

    public static boolean tryReserve(ServerLevel level, UUID ownerId, VortexTier tier) {
        if (level == null || ownerId == null || tier == null) return false;
        long now = level.getGameTime();
        return WindVortexLimitSavedData.get(level).tryReserve(ownerId, tier, now);
    }

    public static void release(ServerLevel level, UUID ownerId, VortexTier tier) {
        if (level == null || ownerId == null || tier == null) return;
        long now = level.getGameTime();
        WindVortexLimitSavedData.get(level).release(ownerId, tier, now);
    }

    public static void clearOwner(ServerLevel level, UUID ownerId) {
        if (level == null || ownerId == null) return;
        WindVortexLimitSavedData.get(level).clearOwner(ownerId);
    }

    public static void clearAllForLevel(ServerLevel level) {
        if (level == null) return;
        WindVortexLimitSavedData.get(level).clearAll();
    }
}
