package com.pgalaxyp.fragmento.rpg.platform.api.world;

import java.util.Optional;

public interface WorldView {
    Optional<EntityHit> raycastLivingEntity(TargetingQuery query);
}