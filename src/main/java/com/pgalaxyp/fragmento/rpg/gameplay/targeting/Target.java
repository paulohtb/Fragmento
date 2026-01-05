package com.pgalaxyp.fragmento.rpg.gameplay.targeting;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public sealed interface Target permits EntityTarget, VirtualTarget {
    Vec3 position();
    Aabb bounds();
    boolean isReal();
    long actorIdOrZero();
}