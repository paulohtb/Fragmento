package com.pgalaxyp.fragmento.core.controller;

import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;

import java.util.function.Function;

public final class CollisionController<T extends SkillEntityBase> extends EntityController<T> {

    @FunctionalInterface
    public interface CollisionCheck {
        LivingEntity hit(Vec3 from, Vec3 to, LivingEntity target);
    }

    private CollisionCheck check;

    private LivingEntity lastHit;
    private boolean blockHit;
    private Vec3 blockHitPos;

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
        Vec3 to = entity.position();

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
            return;
        }

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        if (target == null || !target.isAlive()) return;

        LivingEntity hit = check.hit(from, to, target);
        if (hit != null) lastHit = hit;
    }

    public static CollisionCheck segmentHit(double inflate) {
        return (from, to, target) -> {
            AABB box = target.getBoundingBox().inflate(inflate);
            return box.clip(from, to).isPresent() ? target : null;
        };
    }

    public static CollisionCheck adaptiveHomingHit(
            SkillEntityBase self,
            double baseInflate,
            double speedInflateFactor,
            double endPointExtraRadius
    ) {
        return (from, to, target) -> {
            if (self == null || target == null || !target.isAlive()) return null;

            double speed = self.getDeltaMovement().length();
            double inflate = Math.max(0.0, baseInflate + speed * Math.max(0.0, speedInflateFactor));

            AABB box = target.getBoundingBox().inflate(inflate);

            if (box.clip(from, to).isPresent()) {
                return target;
            }

            if (endPointExtraRadius > 0.0) {
                double r = inflate + endPointExtraRadius;
                double d2 = distanceSqrPointToAabb(to, box);
                if (d2 <= r * r) {
                    return target;
                }
            }

            return null;
        };
    }

    private static double distanceSqrPointToAabb(Vec3 p, AABB aabb) {
        double cx = clamp(p.x, aabb.minX, aabb.maxX);
        double cy = clamp(p.y, aabb.minY, aabb.maxY);
        double cz = clamp(p.z, aabb.minZ, aabb.maxZ);

        double dx = p.x - cx;
        double dy = p.y - cy;
        double dz = p.z - cz;

        return dx * dx + dy * dy + dz * dz;
    }

    private static double clamp(double v, double min, double max) {
        if (v < min) return min;
        return Math.min(v, max);
    }
}
