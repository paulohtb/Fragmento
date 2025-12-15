package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public final class SpawnController<T extends Entity> extends EntityController<T> {

    private final BiConsumer<T, LivingEntity> targetSetter;
    private boolean fired;

    public SpawnController(T entity, BiConsumer<T, LivingEntity> targetSetter) {
        super(entity);
        this.targetSetter = targetSetter;
    }

    @Override
    protected void onTick() {
        if (!fired) fired = true;
    }

    public void initializeSpawn(
            LivingEntity caster,
            LivingEntity target,
            ServerLevel level,
            boolean charged
    ) {
        if (caster == null) return;

        Vec3 eye = caster.getEyePosition();

        float yawRad = (float) Math.toRadians(caster.getYRot());

        Vec3 forward = new Vec3(
                -Math.sin(yawRad),
                0.0,
                Math.cos(yawRad)
        );

        double lenSqr = forward.lengthSqr();
        if (lenSqr < 1.0E-6) {
            forward = new Vec3(0.0, 0.0, 1.0);
        } else {
            forward = forward.normalize();
        }

        Vec3 right = new Vec3(
                forward.z,
                0.0,
                -forward.x
        );

        RandomSource r = level.getRandom();
        double side = r.nextBoolean() ? 1.3 : -1.3;

        Vec3 spawnPos = eye
                .add(forward)
                .add(right.scale(side));

        targetSetter.accept(entity, target);

        Rot rot = computeInitialLookAt(caster, target, spawnPos);

        entity.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, rot.yaw, rot.pitch);

        entity.yRotO = rot.yaw;
        entity.xRotO = rot.pitch;

        level.addFreshEntity(entity);
    }

    private static Rot computeInitialLookAt(LivingEntity caster, LivingEntity target, Vec3 fromPos) {
        Vec3 to;

        if (target != null && target.isAlive()) {
            to = target.getBoundingBox().getCenter();
        } else {
            Vec3 look = caster.getLookAngle();
            if (look.lengthSqr() < 1.0E-6) look = new Vec3(0.0, 0.0, 1.0);
            to = fromPos.add(look.normalize());
        }

        Vec3 delta = to.subtract(fromPos);

        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        double horiz = Math.sqrt(dx * dx + dz * dz);
        if (horiz < 1.0E-6) {
            return new Rot(caster.getYRot(), caster.getXRot());
        }

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, horiz)));

        return new Rot(yaw, pitch);
    }

    private record Rot(float yaw, float pitch) {
    }
}
