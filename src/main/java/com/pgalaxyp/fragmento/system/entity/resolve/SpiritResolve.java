package com.pgalaxyp.fragmento.system.entity.resolve;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public final class SpiritResolve {

    private SpiritResolve() {}

    public static LivingEntity resolveOwner(
            ServerLevel level,
            UUID ownerUuid,
            LivingEntity cached
    ) {
        if (ownerUuid == null || level == null) return null;

        if (cached != null && cached.isAlive() && ownerUuid.equals(cached.getUUID())) {
            return cached;
        }

        Entity e = level.getEntity(ownerUuid);
        if (e instanceof LivingEntity living && living.isAlive()) {
            return living;
        }

        return null;
    }

    public static LivingEntity resolveTarget(
            ServerLevel level,
            int targetId,
            LivingEntity cached
    ) {
        if (targetId <= 0 || level == null) return null;

        if (cached != null && cached.isAlive() && cached.getId() == targetId) {
            return cached;
        }

        Entity e = level.getEntity(targetId);
        if (e instanceof LivingEntity living && living.isAlive()) {
            return living;
        }

        return null;
    }
}