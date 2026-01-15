package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.*;

public final class TargetingResolver {
    private final SingleRaycastTargetingSystem raycastSingle = new SingleRaycastTargetingSystem();

    public TargetResult resolve(TargetingContext context) {
        if (context == null) throw new IllegalArgumentException();

        Optional<ViewRay> rayOpt = context.world().viewRay(context.casterId());
        if (context.spec().mode() == TargetingMode.RAYCAST_SINGLE && rayOpt.isPresent()) {
            Optional<Target> targetOpt = raycastSingle.resolve(context, rayOpt.get());
            if (targetOpt.isPresent()) return directResult(context.casterId(), targetOpt.get());
        }

        return fallbackResult(context, rayOpt.orElse(null));
    }

    private TargetResult directResult(ActorId caster, Target target) {
        if (target instanceof ActorTarget(var targetId) && !targetId.equals(caster)) return TargetResult.direct(target, targetId);
        return TargetResult.direct(target, null);
    }

    private TargetResult fallbackResult(TargetingContext context, ViewRay ray) {
        TargetingFallback policy = context.spec().fallbackPolicy();
        ActorId self = context.casterId();
        if (policy == TargetingFallback.SELF) return TargetResult.fallback(new ActorTarget(self), policy, self);

        if (policy == TargetingFallback.IMAGINARY_POINT && ray != null) {
            double half = context.spec().rangeBlocks() * 0.5;
            return TargetResult.fallback(new PointTarget(ray.pointAt(half)), policy, null);
        }

        return TargetResult.fallback(new ActorTarget(self), TargetingFallback.SELF, self);
    }
}