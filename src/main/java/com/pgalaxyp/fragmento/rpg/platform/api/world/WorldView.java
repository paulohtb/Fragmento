package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import java.util.Optional;

public interface WorldView {
    Optional<EntityHit> raycastLivingEntity(RaycastQuery query);
    boolean isSolidAt(Vec3 position);
}