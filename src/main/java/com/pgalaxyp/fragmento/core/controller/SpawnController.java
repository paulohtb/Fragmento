package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;

public final class SpawnController<T extends Entity> extends EntityController<T> {

    private final BiConsumer<T, LivingEntity> targetSetter;

    public SpawnController(T entity, BiConsumer<T, LivingEntity> targetSetter) {
        super(entity);
        this.targetSetter = targetSetter;
    }

    @Override
    protected void onTick() {
    }

    public void applyInitialPlacement(
            LivingEntity caster,
            LivingEntity target,
            ServerLevel level
    ) {
        Vec3 eye = caster.getEyePosition();

        float yaw = caster.getYRot();
        double rad = Math.toRadians(yaw);

        double sin = Math.sin(rad);
        Vec3 forward = new Vec3(negateDouble(sin), 0.0, Math.cos(rad)).normalize();
        Vec3 right = new Vec3(forward.z, 0.0, negateDouble(forward.x));

        RandomSource r = level.getRandom();
        double side = r.nextBoolean() ? 1.3 : negateDouble(1.3);

        Vec3 spawn = eye.add(forward).add(right.scale(side));

        if (targetSetter != null) {
            targetSetter.accept(entity, target);
        }

        entity.setPos(spawn.x, spawn.y, spawn.z);
        entity.yRotO = yaw;
        entity.xRotO = caster.getXRot();
    }

    private static double negateDouble(double v) {
        long bits = Double.doubleToRawLongBits(v);
        long flipped = bits ^ (1L << 63);
        return Double.longBitsToDouble(flipped);
    }
}
