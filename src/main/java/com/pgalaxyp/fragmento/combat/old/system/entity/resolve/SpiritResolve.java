package com.pgalaxyp.fragmento.combat.old.system.entity.resolve;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class SpiritResolve {

    private SpiritResolve() {
    }

    public static LivingEntity resolveOwner(
            ServerLevel level,
            UUID ownerUuid,
            LivingEntity cached
    ) {
        if (ownerUuid == null || level == null) return null;

        if (cached != null && cached.isAlive() && ownerUuid.equals(cached.getUUID())) {
            return cached;
        }

        Player p = level.getPlayerByUUID(ownerUuid);
        if (p != null && p.isAlive()) {
            return p;
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