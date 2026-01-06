package com.pgalaxyp.fragmento.rpg.gameplay.actor;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record ActorState(Vec3 position, Vec3 lookDirection, Aabb bounds, boolean alive) {
    public ActorState {
        if (position == null) throw new NullPointerException("position");
        if (lookDirection == null) throw new NullPointerException("lookDirection");
        if (bounds == null) throw new NullPointerException("bounds");
    }
}