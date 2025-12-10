package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;

public final class SpawnController<T extends Entity> extends EntityController<T> {

    private boolean fired;
    private final BiConsumer<T, LivingEntity> targetSetter;

    private static final double VERTICAL_MIN = 0;
    private static final double VERTICAL_MAX = 0.75;

    private static final double DEPTH_MIN = 1.25;
    private static final double DEPTH_MAX = 1.3;

    public SpawnController(T entity, BiConsumer<T, LivingEntity> targetSetter) {
        super(entity);
        this.targetSetter = targetSetter;
    }

    @Override
    public void tick() {
        if (!fired) {
            fired = true;
        }
    }

    public void initializeSpawn(LivingEntity caster, LivingEntity target, ServerLevel level, boolean charged) {
        Vec3 pivot = computePivot(caster);
        Vec3 spawnPos = computeSpawnPosition(caster, level, pivot);

        targetSetter.accept(entity, target);

        entity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        if (target != null) {
            Vec3 targetCenter = target.getBoundingBox().getCenter();
            Vec3 dir = targetCenter.subtract(spawnPos);

            double dx = dir.x;
            double dy = dir.y;
            double dz = dir.z;

            double horiz = Math.sqrt(dx * dx + dz * dz);
            if (horiz > 1.0E-6) {
                float yaw = (float) Math.toDegrees(Math.atan2(dx, dz));
                float pitch = (float) Math.toDegrees(Math.atan2(-dy, horiz));

                entity.setYRot(yaw);
                entity.yRotO = yaw;

                entity.setXRot(pitch);
                entity.xRotO = pitch;

                entity.setYHeadRot(yaw);
                entity.setYBodyRot(yaw);
            }
        }

        level.addFreshEntity(entity);
    }

    private Vec3 computePivot(LivingEntity caster) {
        Vec3 eye = caster.getEyePosition();
        Vec3 forward = caster.getLookAngle().normalize();
        return eye.add(forward.scale(0.1));
    }

    private Vec3 computeSpawnPosition(LivingEntity caster, Level level, Vec3 pivot) {
        RandomSource r = level.getRandom();

        Vec3 look = caster.getLookAngle().normalize();
        Vec3 right = new Vec3(look.z, 0, -look.x).normalize();
        Vec3 up = new Vec3(0, 1, 0);

        double minSide = 1.25;
        double maxSide = 1.725;

        double minForward = 0.75;
        double maxForward = 1.5;

        double maxVertical = 0.75;

        double minLateralRatio = 1.0;

        for (int i = 0; i < 20; i++) {
            double depth = Mth.lerp(r.nextDouble(), minForward, maxForward);
            Vec3 forwardOffset = look.scale(depth);

            double side = Mth.lerp(r.nextDouble(), minSide, maxSide);
            if (r.nextBoolean()) {
                side = -side;
            }
            Vec3 lateralOffset = right.scale(side);

            double vertical = Mth.lerp(r.nextDouble(), 0, maxVertical);
            Vec3 verticalOffset = up.scale(vertical);

            Vec3 offset = forwardOffset.add(lateralOffset).add(verticalOffset);

            double projForward = offset.dot(look);
            double totalLenSq = offset.lengthSqr();
            double lateralLen = Math.sqrt(Math.max(0.0, totalLenSq - projForward * projForward));

            if (lateralLen >= minLateralRatio * Math.abs(projForward)) {
                return pivot.add(offset);
            }
        }

        double side = level.getRandom().nextBoolean() ? minSide : -minSide;
        Vec3 fallbackOffset = look.scale(minForward).add(right.scale(side));
        return pivot.add(fallbackOffset);
    }

}
