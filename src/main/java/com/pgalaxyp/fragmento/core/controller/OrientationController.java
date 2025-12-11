package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class OrientationController<T extends Entity> extends EntityController<T> {

    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;

    public OrientationController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    @Override
    public void tick() {
        if (!enabled) return;

        LivingEntity target = targetGetter.apply(entity);
        if (target == null) return;

        Vec3 selfPos = entity.position();
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 delta = targetCenter.subtract(selfPos);

        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        double h = Math.sqrt(dx * dx + dz * dz);
        if (h < 1.0E-6) return;

        float targetYaw = (float) Math.toDegrees(Math.atan2(dx, dz));
        float targetPitch = (float) Math.toDegrees(Math.atan2(-dy, h));

        float cy = entity.getYRot();
        float cp = entity.getXRot();
        float s = 18f;

        float ny = Mth.approachDegrees(cy, targetYaw, s);
        float np = Mth.approachDegrees(cp, targetPitch, s);

        entity.setYRot(ny);
        entity.yRotO = ny;

        entity.setXRot(np);
        entity.xRotO = np;
    }

    @Override
    protected void onTick() {

    }
}
