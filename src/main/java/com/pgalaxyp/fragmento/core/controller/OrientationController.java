package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.function.Supplier;

public final class OrientationController<T extends Entity> extends EntityController<T> {

    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;
    private final Supplier<Vec3> lookPosGetter;

    public OrientationController(
            T entity,
            Function<T, LivingEntity> targetGetter,
            Supplier<Vec3> lookPosGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
        this.lookPosGetter = lookPosGetter;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    @Override
    protected void onTick() {
        if (!enabled) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;

        Vec3 from = entity.position().add(0.0, entity.getBbHeight() * 0.5, 0.0);
        Vec3 to = null;

        if (target != null && target.isAlive()) {
            to = target.getEyePosition();
        } else if (lookPosGetter != null) {
            to = lookPosGetter.get();
        }

        if (to == null) return;

        Vec3 delta = to.subtract(from);

        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        double horiz = Math.sqrt(dx * dx + dz * dz);
        if (horiz < 1.0E-6) return;

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, horiz)));

        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.clamp(pitch, -90.0F, 90.0F);

        applyRotation(yaw, pitch);
    }

    private void applyRotation(float yaw, float pitch) {
        entity.setYRot(yaw);
        entity.setXRot(pitch);

        entity.yRotO = yaw;
        entity.xRotO = pitch;
    }
}
