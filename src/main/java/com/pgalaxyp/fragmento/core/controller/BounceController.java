package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public final class BounceController<T extends Entity> extends EntityController<T> {

    private Vec3 pendingBounce;
    private boolean active;
    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;

    public BounceController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    @Override
    public void tick() {
        if (!enabled) return;
        if (!active) return;

        entity.setDeltaMovement(pendingBounce);
        active = false;
    }

    @Override
    protected void onTick() {

    }

    public void setEnabled(boolean v) {
        enabled = v;
        if (!v) {
            pendingBounce = null;
            active = false;
        }
    }

    public void bounce() {
        LivingEntity target = targetGetter.apply(entity);
        if (target == null) return;

        pendingBounce = calculateBounce(entity, target);
        active = true;
    }

    private Vec3 calculateBounce(Entity self, LivingEntity target) {

        Vec3 current = self.getDeltaMovement();
        double speed = current.length();
        if (speed < 0.01) speed = 0.02;

        Vec3 from = self.position();
        Vec3 to = target.getBoundingBox().getCenter();
        Vec3 dir = from.subtract(to);

        double len = dir.length();
        if (len < 1.0E-6) dir = new Vec3(0, 0.02, 0);
        else dir = dir.normalize();

        Vec3 upward = new Vec3(0, 0.2, 0);
        Vec3 finalDir = dir.add(upward).normalize();

        double scale = 0.2;

        return finalDir.scale(scale);
    }
}
