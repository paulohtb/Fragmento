package com.pgalaxyp.fragmento.core.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.projectile.ProjectileUtil;

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
        return performInternal(caster, range, e -> {
            if (e == null) return false;
            if (e == caster) return false;
            if (!(e instanceof LivingEntity l)) return false;
            return l.isAlive();
        });
    }

    public static Result performPlayersOnly(LivingEntity caster, double range) {
        return performInternal(caster, range, e -> {
            if (e == null) return false;
            if (e == caster) return false;
            if (!(e instanceof Player p)) return false;
            return p.isAlive();
        });
    }

    private static Result performInternal(
            LivingEntity caster,
            double range,
            Predicate<Entity> filter
    ) {
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
        broad = com.pgalaxyp.fragmento.core.util.AabbUtil.expand(broad, 1.0);

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
}