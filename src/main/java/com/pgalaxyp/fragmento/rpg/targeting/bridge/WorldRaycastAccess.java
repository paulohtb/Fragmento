package com.pgalaxyp.fragmento.rpg.targeting.bridge;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.targeting.api.ViewRay;
import java.util.Optional;

public interface WorldRaycastAccess {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}