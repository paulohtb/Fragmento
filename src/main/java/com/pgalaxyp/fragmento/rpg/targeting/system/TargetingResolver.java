package com.pgalaxyp.fragmento.rpg.targeting.system;

import com.pgalaxyp.fragmento.rpg.targeting.api.ActorTarget;
import com.pgalaxyp.fragmento.rpg.targeting.api.PointTarget;
import com.pgalaxyp.fragmento.rpg.targeting.api.Target;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetResult;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingFallbackPolicy;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingMode;
import com.pgalaxyp.fragmento.rpg.targeting.api.ViewRay;
import java.util.Optional;

public final class TargetingResolver {

    private final RaycastTargetingSystem raycastSingle = new RaycastTargetingSystem();

    public TargetResult resolve(TargetingContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        Optional<ViewRay> rayOpt = context.world().viewRay(context.casterId());

        TargetingMode mode = context.spec().mode();
        Optional<Target> direct = Optional.empty();

        if (mode == TargetingMode.RAYCAST_SINGLE && rayOpt.isPresent()) {
            direct = raycastSingle.resolve(context, rayOpt.get());
        }

        if (direct.isPresent()) {
            return TargetResult.direct(direct.get());
        }

        return applyFallback(context, rayOpt);
    }

    private TargetResult applyFallback(TargetingContext context, Optional<ViewRay> rayOpt) {
        TargetingFallbackPolicy policy = context.spec().fallbackPolicy();

        if (policy == TargetingFallbackPolicy.SELF) {
            return TargetResult.fallback(new ActorTarget(context.casterId()), TargetingFallbackPolicy.SELF);
        }

        if (policy == TargetingFallbackPolicy.IMAGINARY_POINT) {
            if (rayOpt.isEmpty()) {
                return TargetResult.fallback(new ActorTarget(context.casterId()), TargetingFallbackPolicy.SELF);
            }
            ViewRay ray = rayOpt.get();
            double half = ((double) context.spec().rangeBlocks()) * 0.5;
            return TargetResult.fallback(new PointTarget(ray.pointAt(half)), TargetingFallbackPolicy.IMAGINARY_POINT);
        }

        return TargetResult.fallback(new ActorTarget(context.casterId()), TargetingFallbackPolicy.SELF);
    }
}