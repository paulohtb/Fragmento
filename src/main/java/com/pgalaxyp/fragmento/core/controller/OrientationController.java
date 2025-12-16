package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;
import java.util.function.Supplier;

public final class OrientationController<T extends net.minecraft.world.entity.Entity>
        extends EntityController<T> {

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

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx));
        yaw = Mth.wrapDegrees(yaw + 270.0F);

        double horiz = Math.sqrt(dx * dx + dz * dz);
        float pitchRaw = (float) Math.toDegrees(Math.atan2(dy, horiz));
        float pitch = Mth.wrapDegrees((float) (360.0 + negateDouble(pitchRaw)));

        entity.setYRot(yaw);
        entity.setXRot(pitch);
    }

    private static double negateDouble(double v) {
        long bits = Double.doubleToRawLongBits(v);
        long flipped = bits ^ (1L << 63);
        return Double.longBitsToDouble(flipped);
    }
}
