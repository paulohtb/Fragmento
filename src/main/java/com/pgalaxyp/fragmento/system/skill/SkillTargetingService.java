package com.pgalaxyp.fragmento.system.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class SkillTargetingService {

    private SkillTargetingService() {
    }

    public static LivingEntity resolveBasicLivingTarget(
            ServerLevel level,
            ServerPlayer caster,
            double range,
            int targetId
    ) {
        if (targetId <= 0) return null;

        Entity e = level.getEntity(targetId);
        if (!(e instanceof LivingEntity living)) return null;
        if (!living.isAlive()) return null;

        if (living instanceof Player) return null;
        if (living.getUUID().equals(caster.getUUID())) return null;

        if (!isWithinRange(caster, living, range)) return null;

        return living;
    }

    public static ServerPlayer resolveSpecialPlayerTargetOrSelf(
            ServerLevel level,
            ServerPlayer caster,
            double range,
            int targetId
    ) {
        if (targetId <= 0) return caster;

        Entity e = level.getEntity(targetId);
        if (!(e instanceof ServerPlayer other)) return caster;
        if (!other.isAlive()) return caster;

        if (!isWithinRange(caster, other, range)) return caster;

        return other;
    }

    private static boolean isWithinRange(ServerPlayer caster, LivingEntity target, double range) {
        double r = Math.max(0.0, range);
        double maxDistSqr = r * r;
        return caster.distanceToSqr(target) <= maxDistSqr;
    }
}
