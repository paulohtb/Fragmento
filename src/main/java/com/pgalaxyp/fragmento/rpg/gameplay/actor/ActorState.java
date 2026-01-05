package com.pgalaxyp.fragmento.rpg.gameplay.actor;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record ActorState(Vec3 position, Aabb bounds, boolean alive) {
    public ActorState {
        if (position == null) throw new NullPointerException("position");
        if (bounds == null) throw new NullPointerException("bounds");
    }
}