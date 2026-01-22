package com.pgalaxyp.fragmento.combat.targeting.bridge;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.targeting.api.*;

import java.util.*;

public interface WorldRaycastAccess {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}