package com.pgalaxyp.fragmento.core.controller;

import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public final class CollisionController<T extends SkillEntityBase>
        extends EntityController<T> {

    @FunctionalInterface
    public interface CollisionCheck<T extends SkillEntityBase> {
        LivingEntity hit(Vec3 from, Vec3 to, LivingEntity target);
    }

    private CollisionCheck<T> check;
    private LivingEntity lastHit;

    private final Function<T, LivingEntity> targetGetter;

    public CollisionController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public boolean hasCollision() {
        return lastHit != null;
    }

    public LivingEntity getCollisionTarget() {
        return lastHit;
    }

    public void resetCollision() {
        lastHit = null;
    }

    public void setCollisionCheck(CollisionCheck<T> check) {
        this.check = check;
    }

    @Override
    protected void onTick() {
        if (check == null || lastHit != null) return;

        Vec3 from = entity.getPrevLogicPos();
        Vec3 to = entity.getLogicPos();

        LivingEntity target = targetGetter.apply(entity);
        if (target != null && target.isAlive()) {
            LivingEntity hit = check.hit(from, to, target);
            if (hit != null) {
                lastHit = hit;
            }
        }
    }

    public CollisionCheck<T> segment(double inflate) {
        return (from, to, target) -> {
            AABB box = target.getBoundingBox().inflate(inflate);
            return box.clip(from, to).isPresent() ? target : null;
        };
    }
}
