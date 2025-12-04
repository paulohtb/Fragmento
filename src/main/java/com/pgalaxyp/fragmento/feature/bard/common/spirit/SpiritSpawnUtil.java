package com.pgalaxyp.fragmento.feature.bard.common.spirit;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class SpiritSpawnUtil {

    private SpiritSpawnUtil() {
    }

    public static Vec3 aroundTarget(
            LivingEntity caster,
            LivingEntity target,
            Level level,
            double minForward,
            double maxForward,
            double maxSide
    ) {

        double casterHeight = caster.getBbHeight();
        double minY = caster.getY() + casterHeight * 0.75D;
        double maxY = caster.getY() + casterHeight * 1.25D;
        double spawnY = minY + level.random.nextDouble() * (maxY - minY);

        Vec3 casterEye = caster.getEyePosition(1.0F);
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 dir = targetCenter.subtract(casterEye);
        Vec3 horizontalDir = new Vec3(dir.x, 0.0D, dir.z);
        if (horizontalDir.lengthSqr() < 1.0E-6D) {
            horizontalDir = new Vec3(0.0D, 0.0D, 1.0D);
        }
        horizontalDir = horizontalDir.normalize();
        Vec3 right = new Vec3(-horizontalDir.z, 0.0D, horizontalDir.x);

        double sideOffset = ((level.random.nextDouble() * 2.0D) - 1.0D) * maxSide;
        double forwardOffset = minForward + level.random.nextDouble() * (maxForward - minForward);
        Vec3 offsetHorizontal = horizontalDir.scale(forwardOffset).add(right.scale(sideOffset));

        Vec3 basePos = casterEye.add(offsetHorizontal);

        return new Vec3(
                basePos.x,
                spawnY,
                basePos.z
        );
    }
}
