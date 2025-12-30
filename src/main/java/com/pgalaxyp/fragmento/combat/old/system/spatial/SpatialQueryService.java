package com.pgalaxyp.fragmento.combat.old.system.spatial;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public final class SpatialQueryService {

    public List<LivingEntity> queryLiving(
            ServerLevel level,
            AABB area,
            Predicate<LivingEntity> filter,
            int limit
    ) {
        List<LivingEntity> list =
                level.getEntitiesOfClass(LivingEntity.class, area, filter);

        if (list.size() <= limit) {
            return list;
        }

        return list.subList(0, limit);
    }

    public LivingEntity resolveEntity(ServerLevel level, int id) {
        if (level == null || id <= 0) return null;
        Entity e = level.getEntity(id);
        return e instanceof LivingEntity l ? l : null;
    }
}