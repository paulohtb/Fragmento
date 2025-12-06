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

public abstract class RaycastBase {

    private double range = 8.0D;

    public void setRange(double value) {
        this.range = value;
    }

    public double getRange() {
        return this.range;
    }

    public final boolean execute(LivingEntity caster) {
        Level level = caster.level();
        if (level.isClientSide()) {
            return false;
        }

        Vec3 start = caster.getEyePosition(1.0F);
        Vec3 look = caster.getLookAngle().normalize();
        Vec3 end = start.add(look.scale(this.range));

        HitResult blockHit = level.clip(
                new ClipContext(
                        start,
                        end,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        caster
                )
        );

        double maxDistance = this.range;
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
                onHitEntity(caster, living, entityHit.getLocation());
                return true;
            }
        }

        onHitPosition(caster, limitedEnd);
        return false;
    }

    protected abstract void onHitEntity(LivingEntity caster, LivingEntity target, Vec3 hitPos);

    protected abstract void onHitPosition(LivingEntity caster, Vec3 hitPos);
}
