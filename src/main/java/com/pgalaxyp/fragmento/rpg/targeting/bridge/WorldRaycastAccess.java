package com.pgalaxyp.fragmento.rpg.targeting.bridge;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface WorldRaycastAccess {
    Optional<ViewRay> viewRay(ActorId casterId);
    Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks);
}