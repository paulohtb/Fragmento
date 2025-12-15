package com.pgalaxyp.fragmento.core.controller;

import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public final class AutoMovementController<T extends SkillEntityBase>
        extends EntityController<T> {

    public interface Movement<T extends SkillEntityBase> {
        Vec3 compute(T self, LivingEntity target, int age);
    }

    private Movement<T> movement;
    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;
    private final Function<T, Integer> ageGetter;

    public AutoMovementController(
            T entity,
            Function<T, LivingEntity> targetGetter,
            Function<T, Integer> ageGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
        this.ageGetter = ageGetter;
    }

    public void setMovement(Movement<T> movement) {
        this.movement = movement;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    @Override
    protected void onTick() {
        if (!enabled || movement == null) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        int age = ageGetter != null ? ageGetter.apply(entity) : 0;

        Vec3 next = movement.compute(entity, target, age);
        if (next == null) return;

        entity.setLogicPos(next);
    }
}
