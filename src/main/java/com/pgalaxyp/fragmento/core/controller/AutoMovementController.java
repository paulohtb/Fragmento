package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public final class AutoMovementController<T extends net.minecraft.world.entity.Entity>
        extends EntityController<T> {

    public interface Movement<T> {
        Vec3 compute(T self, LivingEntity target);
    }

    private Movement<T> movement;
    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;

    public AutoMovementController(
            T entity,
            Function<T, LivingEntity> targetGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setMovement(Movement<T> movement) {
        this.movement = movement;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        if (!enabled) {
            entity.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    protected void onTick() {
        if (!enabled || movement == null) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        Vec3 next = movement.compute(entity, target);
        if (next == null) return;

        Vec3 cur = entity.position();
        Vec3 delta = next.subtract(cur);

        if (delta.lengthSqr() < 1.0E-12) return;

        entity.setDeltaMovement(delta);
        entity.move(MoverType.SELF, delta);
    }
}
