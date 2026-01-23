package com.pgalaxyp.fragmento.combat.targetingModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.util.ViewRay;
import java.util.Optional;

public interface TargetingPort {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}