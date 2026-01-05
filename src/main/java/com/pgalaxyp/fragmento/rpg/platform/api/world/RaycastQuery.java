package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record RaycastQuery(Vec3 origin, Vec3 direction, double minDistance, double maxDistance) {
    public RaycastQuery {
        if (maxDistance < minDistance) throw new IllegalArgumentException("maxDistance < minDistance");
    }
}