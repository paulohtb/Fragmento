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

import java.util.function.Predicate;

public final class RaycastUtil {

    public record Result(LivingEntity target, Vec3 hitPosition) {
        public boolean hasTarget() {
            return target != null;
        }
    }

    private RaycastUtil() {
    }

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

        AABB broad = caster.getBoundingBox().expandTowards(look.scale(max));
        broad = expand(broad, 1.0);

        Predicate<Entity> filter = new Predicate<>() {
            @Override
            public boolean test(Entity e) {
                if (e == null) return false;
                if (e == caster) return false;
                if (!(e instanceof LivingEntity l)) return false;
                return l.isAlive();
            }
        };

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                level,
                caster,
                eye,
                limitedEnd,
                broad,
                filter
        );

        if (hit != null && hit.getEntity() instanceof LivingEntity living) {
            return new Result(living, hit.getLocation());
        }

        return new Result(null, limitedEnd);
    }

    private static AABB expand(AABB box, double amount) {
        if (box == null) return null;
        if (amount <= 0.0) return box;
        double a = amount;
        return new AABB(
                box.minX + MathUtil.negate(a), box.minY + MathUtil.negate(a), box.minZ + MathUtil.negate(a),
                box.maxX + a, box.maxY + a, box.maxZ + a
        );
    }
}