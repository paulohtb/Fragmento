package com.pgalaxyp.fragmento.core.controller;

import com.pgalaxyp.fragmento.core.util.MathUtil;
import java.util.function.Function;
import com.pgalaxyp.fragmento.system.entity.SkillEntityBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class CollisionController<T extends SkillEntityBase> extends EntityController<T> {

    public record CollisionResult(LivingEntity entity, double t) {
    }

    @FunctionalInterface
    public interface CollisionCheck {
        CollisionResult hit(Vec3 from, Vec3 to, LivingEntity target);
    }

    private CollisionCheck check;

    private LivingEntity lastHit;
    private boolean blockHit;
    private Vec3 blockHitPos;

    private Vec3 collisionMotionDir;
    private Vec3 collisionImpactPos;

    private boolean enabled;

    private final Function<T, LivingEntity> targetGetter;

    public CollisionController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setEnabled(boolean value) {
        if (!value) resetCollision();
        enabled = value;
    }

    public void resetCollision() {
        lastHit = null;
        blockHit = false;
        blockHitPos = null;
        collisionMotionDir = null;
        collisionImpactPos = null;
    }

    public boolean hasCollision() {
        return lastHit != null;
    }

    public LivingEntity getCollisionTarget() {
        return lastHit;
    }

    public boolean hasBlockCollision() {
        return blockHit;
    }

    public Vec3 getBlockHitPos() {
        return blockHitPos;
    }

    public Vec3 getCollisionMotionDir() {
        return collisionMotionDir;
    }

    public Vec3 getCollisionImpactPos() {
        return collisionImpactPos;
    }

    public boolean hasAnyCollision() {
        return lastHit != null || blockHit;
    }

    public void setCollisionCheck(CollisionCheck check) {
        this.check = check;
        resetCollision();
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (!enabled) return;
        if (check == null) return;
        if (lastHit != null || blockHit) return;

        Vec3 from = entity.getPrevPos();
        Vec3 to = entity.position().add(entity.getDeltaMovement());
        Vec3 seg = to.subtract(from);

        collisionMotionDir = null;
        if (seg.lengthSqr() > 1.0E-12) {
            collisionMotionDir = seg.normalize();
        }

        HitResult block = entity.level().clip(
                new ClipContext(
                        from,
                        to,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        entity
                )
        );

        if (block.getType() != HitResult.Type.MISS) {
            blockHit = true;
            blockHitPos = block.getLocation();
            collisionImpactPos = blockHitPos;
            entity.setPos(blockHitPos.x, blockHitPos.y, blockHitPos.z);
            entity.setDeltaMovement(Vec3.ZERO);
            return;
        }

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        if (target == null || !target.isAlive()) return;

        CollisionResult hit = check.hit(from, to, target);
        if (hit == null || hit.entity() == null) return;

        lastHit = hit.entity();

        double t = hit.t();
        if (t < 0.0) t = 0.0;
        if (t > 1.0) t = 1.0;

        Vec3 impact = from.add(seg.scale(t));
        collisionImpactPos = impact;
        entity.setPos(impact.x, impact.y, impact.z);
        entity.setDeltaMovement(Vec3.ZERO);
    }

    public static CollisionCheck segmentHit() {
        return new CollisionCheck() {
            @Override
            public CollisionResult hit(Vec3 from, Vec3 to, LivingEntity target) {
                if (from == null || to == null || target == null || !target.isAlive()) return null;

                AABB box = target.getBoundingBox();

                double t = segmentAabbFirstHitT(from, to, box);
                if (Double.isNaN(t)) return null;

                return new CollisionResult(target, t);
            }
        };
    }

    public static CollisionCheck adaptiveHomingHit(SkillEntityBase self) {
        return new CollisionCheck() {
            @Override
            public CollisionResult hit(Vec3 from, Vec3 to, LivingEntity target) {
                if (self == null || from == null || to == null || target == null || !target.isAlive()) return null;

                AABB box = target.getBoundingBox();

                double t = segmentAabbFirstHitT(from, to, box);
                if (Double.isNaN(t)) return null;

                return new CollisionResult(target, t);
            }
        };
    }

    private static double segmentAabbFirstHitT(Vec3 from, Vec3 to, AABB box) {
        if (from == null || to == null || box == null) return Double.NaN;

        double x0 = from.x;
        double y0 = from.y;
        double z0 = from.z;

        double dx = to.x + MathUtil.negate(x0);
        double dy = to.y + MathUtil.negate(y0);
        double dz = to.z + MathUtil.negate(z0);

        double tMin = 0.0;
        double tMax = 1.0;

        double t;

        t = axisInterval(x0, dx, box.minX, box.maxX);
        if (Double.isNaN(t)) return Double.NaN;
        tMin = Math.max(tMin, t);

        t = axisIntervalMax(x0, dx, box.minX, box.maxX);
        if (Double.isNaN(t)) return Double.NaN;
        tMax = Math.min(tMax, t);

        if (tMax < tMin) return Double.NaN;

        t = axisInterval(y0, dy, box.minY, box.maxY);
        if (Double.isNaN(t)) return Double.NaN;
        tMin = Math.max(tMin, t);

        t = axisIntervalMax(y0, dy, box.minY, box.maxY);
        if (Double.isNaN(t)) return Double.NaN;
        tMax = Math.min(tMax, t);

        if (tMax < tMin) return Double.NaN;

        t = axisInterval(z0, dz, box.minZ, box.maxZ);
        if (Double.isNaN(t)) return Double.NaN;
        tMin = Math.max(tMin, t);

        t = axisIntervalMax(z0, dz, box.minZ, box.maxZ);
        if (Double.isNaN(t)) return Double.NaN;
        tMax = Math.min(tMax, t);

        if (tMax < tMin) return Double.NaN;

        return tMin;
    }

    private static double axisInterval(double origin, double d, double min, double max) {
        double eps = 1.0E-12;
        if (Math.abs(d) <= eps) {
            if (origin < min || origin > max) return Double.NaN;
            return 0.0;
        }

        double inv = 1.0 / d;
        double t1 = (min + MathUtil.negate(origin)) * inv;
        double t2 = (max + MathUtil.negate(origin)) * inv;

        if (t1 > t2) {
            double tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        return t1;
    }

    private static double axisIntervalMax(double origin, double d, double min, double max) {
        double eps = 1.0E-12;
        if (Math.abs(d) <= eps) {
            if (origin < min || origin > max) return Double.NaN;
            return 1.0;
        }

        double inv = 1.0 / d;
        double t1 = (min + MathUtil.negate(origin)) * inv;
        double t2 = (max + MathUtil.negate(origin)) * inv;

        if (t1 > t2) {
            double tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        return t2;
    }
}
