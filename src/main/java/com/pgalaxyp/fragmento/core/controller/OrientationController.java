package com.pgalaxyp.fragmento.core.controller;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class OrientationController<T extends net.minecraft.world.entity.Entity> extends EntityController<T> {

    private final Function<T, LivingEntity> targetGetter;
    private final Supplier<Vec3> lookAtGetter;

    private boolean enabled = true;

    public OrientationController(
            T entity,
            Function<T, LivingEntity> targetGetter,
            Supplier<Vec3> lookAtGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
        this.lookAtGetter = lookAtGetter;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (!enabled) return;

        Vec3 lookAt = lookAtGetter != null ? lookAtGetter.get() : null;
        if (lookAt == null) {
            LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
            if (target == null) return;
            lookAt = target.getBoundingBox().getCenter();
        }

        Vec3 dir = lookAt.subtract(entity.position());
        if (dir.lengthSqr() < 0.000001) return;

        double dx = dir.x;
        double dz = dir.z;
        double dy = dir.y;

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        yaw = Mth.wrapDegrees(yaw);

        double horiz = Math.sqrt(dx * dx + dz * dz);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, horiz)));
        pitch = Mth.wrapDegrees(pitch);

        entity.setYRot(yaw);
        entity.setXRot(pitch);
    }
}
