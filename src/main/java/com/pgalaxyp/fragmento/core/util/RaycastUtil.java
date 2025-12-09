package com.pgalaxyp.fragmento.core.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class RaycastUtil {

    public record Result(LivingEntity target, Vec3 hitPosition) {
        public boolean hasTarget() {
            return target != null;
        }
    }

    private RaycastUtil() {}

    public static Result perform(LivingEntity caster, double range) {
        Level level = caster.level();

        Vec3 eye = caster.getEyePosition();
        Vec3 look = caster.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(range));

        HitResult block = level.clip(
                new ClipContext(
                        eye,
                        end,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        caster
                )
        );

        double max = range;

        if (block.getType() != HitResult.Type.MISS) {
            double dist = block.getLocation().distanceTo(eye);
            if (dist < max) {
                max = dist;
            }
        }

        Vec3 limitedEnd = eye.add(look.scale(max));

        AABB broad = caster.getBoundingBox()
                .expandTowards(look.scale(max))
                .inflate(1.0);

        EntityHitResult rawHit = ProjectileUtil.getEntityHitResult(
                level,
                caster,
                eye,
                limitedEnd,
                broad,
                e -> e instanceof LivingEntity l && l.isAlive() && e != caster
        );

        if (rawHit != null) {
            Entity ent = rawHit.getEntity();
            if (ent instanceof LivingEntity living) {
                return new Result(living, rawHit.getLocation());
            }
        }

        double bestDist = Double.MAX_VALUE;
        LivingEntity best = null;
        Vec3 bestPos = null;

        for (Entity e : level.getEntities(null, broad)) {
            if (!(e instanceof LivingEntity target)) continue;
            if (target == caster) continue;
            if (!target.isAlive()) continue;

            AABB box = target.getBoundingBox().inflate(0.2);

            Vec3 hit = box.clip(eye, limitedEnd).orElse(null);
            if (hit == null) {
                if (box.contains(limitedEnd)) {
                    hit = limitedEnd;
                }
            }

            if (hit == null) continue;

            double d = hit.distanceTo(eye);
            if (d < bestDist) {
                bestDist = d;
                best = target;
                bestPos = hit;
            }
        }

        if (best != null) {
            return new Result(best, bestPos);
        }

        return new Result(null, limitedEnd);
    }
}
