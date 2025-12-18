package com.pgalaxyp.fragmento.system.entity.movement;

import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SpiritMovementPatterns {

    private static final double DIST_EPS = 0.000000000001;

    private SpiritMovementPatterns() {
    }

    public static boolean chaseLivingTarget(
            SpiritContext ctx,
            MovementPlan movement,
            LookPlan look,
            LivingEntity target,
            int time,
            int duration,
            double yOffset,
            boolean lookAtCenter
    ) {
        if (ctx == null || movement == null || look == null) return false;
        if (target == null || !target.isAlive()) return false;

        Vec3 center = target.getBoundingBox().getCenter();
        Vec3 targetPos = center.add(0.0, yOffset, 0.0);
        Vec3 toTarget = targetPos.subtract(ctx.pos);

        look.kind = LookPlan.Kind.TO_POS;
        look.lookAtPos = lookAtCenter ? center : targetPos;

        double dist = toTarget.length();
        if (dist > DIST_EPS) {
            double progress = (double) time / (double) duration;
            double maxStep = progress >= 0.5 ? 1.75 : 1.0;
            double step = Math.min(maxStep, dist);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = toTarget.scale(step / dist);
        }

        return true;
    }
}