package com.pgalaxyp.fragmento.combat.targeting.bridge;

import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public interface WorldRaycastAccess {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}