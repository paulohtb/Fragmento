package com.pgalaxyp.fragmento.NEW.newnew;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.List;

public final class RayTraceFinder {

    private RayTraceFinder() {}

    public static LivingEntity findTarget(Player player, double range) {

        Level level = player.level();
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(range));

        AABB box = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0);

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e.isAlive() && e != player
        );

        LivingEntity best = null;
        double bestDist = range * range;

        for (LivingEntity e : entities) {
            AABB eBox = e.getBoundingBox().inflate(0.3);
            var hit = eBox.clip(eye, end);
            if (hit.isPresent()) {
                double dist = eye.distanceToSqr(hit.get());
                if (dist < bestDist) {
                    bestDist = dist;
                    best = e;
                }
            }
        }

        return best;
    }
}