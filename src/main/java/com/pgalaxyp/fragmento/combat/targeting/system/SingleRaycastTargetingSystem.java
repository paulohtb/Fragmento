package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;
import java.util.*;

public final class SingleRaycastTargetingSystem {
    public Optional<Target> resolve(TargetingContext context, ViewRay ray) {
        if (context == null || ray == null) throw new IllegalArgumentException();

        double range = context.spec().rangeBlocks();
        Optional<RaycastHit> hitOpt = context.world().raycastFirstHit(context.casterId(), ray, range);
        if (hitOpt.isEmpty()) return Optional.empty();

        RaycastHit hit = hitOpt.get();
        if (hit instanceof RaycastEntityHit eh) return Optional.of(new ActorTarget(eh.actorId()));
        if (hit instanceof RaycastBlockHit bh) return Optional.of(new PointTarget(bh.hitPosition()));

        return Optional.empty();
    }
}