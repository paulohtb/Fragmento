package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import java.util.Optional;

public interface WorldView {
    Optional<EntityHit> raycastLivingEntity(Vec3 origin, Vec3 direction, double min, double max);
    boolean isSolidAt(Vec3 position);
}