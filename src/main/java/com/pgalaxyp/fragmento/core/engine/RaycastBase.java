package com.pgalaxyp.fragmento.core.engine;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class RaycastBase {

    public record Result(LivingEntity hitEntity, Vec3 hitPosition) {

        public boolean hasEntityHit() {
                return this.hitEntity != null;
            }
        }

    private RaycastBase() {
    }

    public static Result perform(LivingEntity caster, double range) {
        Level level = caster.level();

        Vec3 start = caster.getEyePosition(1.0F);
        Vec3 look = caster.getLookAngle().normalize();
        Vec3 end = start.add(look.scale(range));

        HitResult blockHit = level.clip(
                new ClipContext(
                        start,
                        end,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        caster
                )
        );

        double maxDistance = range;
        if (blockHit.getType() != HitResult.Type.MISS) {
            double dist = blockHit.getLocation().distanceTo(start);
            if (dist < maxDistance) {
                maxDistance = dist;
            }
        }

        Vec3 limitedEnd = start.add(look.scale(maxDistance));

        AABB searchBox = caster.getBoundingBox()
                .expandTowards(look.scale(maxDistance))
                .inflate(0.5D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                caster,
                start,
                limitedEnd,
                searchBox,
                e -> e instanceof LivingEntity living
                        && living.isAlive()
                        && e != caster
        );

        if (entityHit != null) {
            Entity hit = entityHit.getEntity();
            if (hit instanceof LivingEntity living) {
                return new Result(living, entityHit.getLocation());
            }
        }

        return new Result(null, limitedEnd);
    }
}
