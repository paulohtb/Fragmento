package com.pgalaxyp.fragmento.combat.engine.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class SkillTargetingService {

    private SkillTargetingService() {
    }

    public static LivingEntity resolveLivingTargetNoPlayers(
            ServerLevel level,
            ServerPlayer caster,
            double range,
            long targetEntityId
    ) {
        if (level == null || caster == null) {
            return null;
        }
        if (targetEntityId <= 0L) {
            return null;
        }

        Entity e = level.getEntity((int) targetEntityId);
        if (!(e instanceof LivingEntity living)) {
            return null;
        }
        if (!living.isAlive()) {
            return null;
        }
        if (living instanceof Player) {
            return null;
        }
        if (living.getUUID().equals(caster.getUUID())) {
            return null;
        }

        double r = Math.max(0.0, range);
        double maxDistSqr = r * r;
        if (caster.distanceToSqr(living) > maxDistSqr) {
            return null;
        }

        return living;
    }
}