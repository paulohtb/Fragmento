package com.pgalaxyp.fragmento.system.entity.controller;

import com.pgalaxyp.fragmento.core.util.MathUtil;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import com.pgalaxyp.fragmento.content.bard.entity.BardSpiritImpactService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class SpiritImpactController {

    private boolean impacted;

    public void reset() {
        impacted = false;
    }

    public void tick(
            ServerLevel level,
            NewwSpiritEntityBase entity,
            SpiritBehavior behavior,
            SpiritContext ctx
    ) {
        if (impacted) return;
        if (level == null || entity == null || behavior == null || ctx == null) return;

        SkillMode mode = ctx.mode;
        if (mode == SkillMode.SPECIAL) return;

        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return;

        AABB moving = entity.getBoundingBox();
        AABB targetBox = target.getBoundingBox();

        if (moving.intersects(targetBox)) {
            Vec3 sep = minimalSeparation(moving, targetBox);
            if (sep != null && sep.lengthSqr() > 1.0E-12) {
                entity.setPos(
                        entity.getX() + sep.x,
                        entity.getY() + sep.y,
                        entity.getZ() + sep.z
                );
            }

            entity.setDeltaMovement(Vec3.ZERO);
            commitImpact(entity, behavior, ctx, target, entity.position());
            return;
        }

        Vec3 spiritDelta = entity.getDeltaMovement();
        if (spiritDelta == null) return;

        Vec3 targetDelta = target.getDeltaMovement();
        if (targetDelta == null) targetDelta = Vec3.ZERO;

        Vec3 rel = new Vec3(
                spiritDelta.x + MathUtil.negate(targetDelta.x),
                spiritDelta.y + MathUtil.negate(targetDelta.y),
                spiritDelta.z + MathUtil.negate(targetDelta.z)
        );

        if (rel.lengthSqr() <= 1.0E-12) return;

        double t = firstContactTime(moving, targetBox, rel.x, rel.y, rel.z);
        if (Double.isNaN(t)) return;

        if (mode == SkillMode.BASIC) {
            Vec3 clamped = spiritDelta.scale(t);
            entity.setDeltaMovement(clamped);

            Vec3 hitPos = entity.position().add(clamped);
            commitImpact(entity, behavior, ctx, target, hitPos);
            return;
        }

        Vec3 hitPos = entity.position().add(spiritDelta.scale(t));
        commitImpact(entity, behavior, ctx, target, hitPos);
    }

    private void commitImpact(
            NewwSpiritEntityBase entity,
            SpiritBehavior behavior,
            SpiritContext ctx,
            LivingEntity target,
            Vec3 hitPos
    ) {
        impacted = true;

        ImpactResult impact = ImpactResult.entity(target, hitPos);
        behavior.onImpact(ctx, impact);

        BardSpiritImpactService.handle(entity, ctx, target);
    }

    private static double firstContactTime(
            AABB moving,
            AABB target,
            double dx,
            double dy,
            double dz
    ) {
        double entryX;
        double exitX;

        if (dx == 0.0) {
            if (moving.maxX <= target.minX || moving.minX >= target.maxX) return Double.NaN;
            entryX = Double.NEGATIVE_INFINITY;
            exitX = Double.POSITIVE_INFINITY;
        } else {
            double inv = 1.0 / dx;
            double t1 = (target.minX + MathUtil.negate(moving.maxX)) * inv;
            double t2 = (target.maxX + MathUtil.negate(moving.minX)) * inv;
            if (t1 > t2) {
                double tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            entryX = t1;
            exitX = t2;
        }

        double entryY;
        double exitY;

        if (dy == 0.0) {
            if (moving.maxY <= target.minY || moving.minY >= target.maxY) return Double.NaN;
            entryY = Double.NEGATIVE_INFINITY;
            exitY = Double.POSITIVE_INFINITY;
        } else {
            double inv = 1.0 / dy;
            double t1 = (target.minY + MathUtil.negate(moving.maxY)) * inv;
            double t2 = (target.maxY + MathUtil.negate(moving.minY)) * inv;
            if (t1 > t2) {
                double tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            entryY = t1;
            exitY = t2;
        }

        double entryZ;
        double exitZ;

        if (dz == 0.0) {
            if (moving.maxZ <= target.minZ || moving.minZ >= target.maxZ) return Double.NaN;
            entryZ = Double.NEGATIVE_INFINITY;
            exitZ = Double.POSITIVE_INFINITY;
        } else {
            double inv = 1.0 / dz;
            double t1 = (target.minZ + MathUtil.negate(moving.maxZ)) * inv;
            double t2 = (target.maxZ + MathUtil.negate(moving.minZ)) * inv;
            if (t1 > t2) {
                double tmp = t1;
                t1 = t2;
                t2 = tmp;
            }
            entryZ = t1;
            exitZ = t2;
        }

        double entry = Math.max(entryX, Math.max(entryY, entryZ));
        double exit = Math.min(exitX, Math.min(exitY, exitZ));

        if (entry > exit) return Double.NaN;
        if (exit < 0.0) return Double.NaN;
        if (entry > 1.0) return Double.NaN;

        if (entry < 0.0) return 0.0;
        return entry;
    }

    private static Vec3 minimalSeparation(AABB moving, AABB target) {
        double pushNegX = target.minX + MathUtil.negate(moving.maxX);
        double pushPosX = target.maxX + MathUtil.negate(moving.minX);
        double pushX = Math.abs(pushNegX) < Math.abs(pushPosX) ? pushNegX : pushPosX;

        double pushNegY = target.minY + MathUtil.negate(moving.maxY);
        double pushPosY = target.maxY + MathUtil.negate(moving.minY);
        double pushY = Math.abs(pushNegY) < Math.abs(pushPosY) ? pushNegY : pushPosY;

        double pushNegZ = target.minZ + MathUtil.negate(moving.maxZ);
        double pushPosZ = target.maxZ + MathUtil.negate(moving.minZ);
        double pushZ = Math.abs(pushNegZ) < Math.abs(pushPosZ) ? pushNegZ : pushPosZ;

        double ax = Math.abs(pushX);
        double ay = Math.abs(pushY);
        double az = Math.abs(pushZ);

        if (ax <= ay && ax <= az) {
            return new Vec3(pushX, 0.0, 0.0);
        }

        if (ay <= ax && ay <= az) {
            return new Vec3(0.0, pushY, 0.0);
        }

        return new Vec3(0.0, 0.0, pushZ);
    }
}