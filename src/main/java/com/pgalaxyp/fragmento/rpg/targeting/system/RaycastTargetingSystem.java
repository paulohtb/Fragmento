package com.pgalaxyp.fragmento.rpg.targeting.system;

import com.pgalaxyp.fragmento.rpg.targeting.api.ActorTarget;
import com.pgalaxyp.fragmento.rpg.targeting.api.PointTarget;
import com.pgalaxyp.fragmento.rpg.targeting.api.Target;
import com.pgalaxyp.fragmento.rpg.targeting.api.ViewRay;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastBlockHit;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastEntityHit;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastHit;
import java.util.Optional;

public final class RaycastTargetingSystem {

    public Optional<Target> resolve(TargetingContext context, ViewRay ray) {
        if (context == null || ray == null) {
            throw new IllegalArgumentException();
        }

        double range = context.spec().rangeBlocks();
        Optional<RaycastHit> hitOpt = context.world().raycastFirstHit(context.casterId(), ray, range);
        if (hitOpt.isEmpty()) {
            return Optional.empty();
        }

        RaycastHit hit = hitOpt.get();
        if (hit instanceof RaycastEntityHit eh) {
            return Optional.of(new ActorTarget(eh.actorId()));
        }
        if (hit instanceof RaycastBlockHit bh) {
            return Optional.of(new PointTarget(bh.hitPosition()));
        }

        return Optional.empty();
    }
}