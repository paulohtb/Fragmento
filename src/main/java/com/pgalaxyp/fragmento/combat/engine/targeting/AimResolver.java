package com.pgalaxyp.fragmento.combat.engine.targeting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;

import java.util.List;

public final class AimResolver {

    private static final double DOT_MIN = 0.85;

    public record Aim(
            LivingEntity target,
            Vec3 point,
            Kind kind
    ) {
        public enum Kind {
            TARGET,
            BLOCK,
            EMPTY
        }

        public Vec3 aimPoint() {
            if (target != null && target.isAlive()) {
                return target.getBoundingBox().getCenter();
            }
            return point;
        }
    }

    public Aim resolve(ServerPlayer player, double maxRange, double emptyRange) {
        if (player == null) {
            return new Aim(null, Vec3.ZERO, Aim.Kind.EMPTY);
        }

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();

        AABB box = player.getBoundingBox()
                .expandTowards(look.scale(maxRange))
                .inflate(1.5);

        List<LivingEntity> entities =
                player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        box,
                        e -> e.isAlive() && e != player
                );

        LivingEntity best = null;
        double bestDist = maxRange * maxRange;

        for (LivingEntity e : entities) {
            Vec3 center = e.getBoundingBox().getCenter();
            Vec3 dir = center.subtract(eye);
            double dist = dir.lengthSqr();
            if (dist > bestDist) continue;

            double dot = look.dot(dir.normalize());
            if (dot < DOT_MIN) continue;

            best = e;
            bestDist = dist;
        }

        if (best != null) {
            return new Aim(best, best.getBoundingBox().getCenter(), Aim.Kind.TARGET);
        }

        HitResult hit = player.pick(maxRange, 0.0F, false);
        if (hit instanceof BlockHitResult bhr) {
            return new Aim(null, Vec3.atCenterOf(bhr.getBlockPos()), Aim.Kind.BLOCK);
        }

        return new Aim(null, eye.add(look.scale(emptyRange)), Aim.Kind.EMPTY);
    }
}