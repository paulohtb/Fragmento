package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Aabb;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public sealed interface Target permits EntityTarget, VirtualTarget {
    Vec3 position();
    Aabb bounds();
    boolean isReal();
    long actorIdOrZero();
}