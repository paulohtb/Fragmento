package com.pgalaxyp.fragmento.combat.engine.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public final class TargetResolver {

    public LivingEntity findFirst(
            ServerLevel level,
            Vec3 origin,
            double radius,
            Predicate<LivingEntity> filter
    ) {
        AABB box = new AABB(
                origin.x - radius, origin.y - radius, origin.z - radius,
                origin.x + radius, origin.y + radius, origin.z + radius
        );

        List<LivingEntity> list =
                level.getEntitiesOfClass(LivingEntity.class, box, filter);

        if (list.isEmpty()) {
            return null;
        }

        return list.getFirst();
    }
}