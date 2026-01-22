package com.pgalaxyp.fragmento.combat.targeting.port;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.targeting.api.ViewRay;
import java.util.Optional;

public interface TargetingPort {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}