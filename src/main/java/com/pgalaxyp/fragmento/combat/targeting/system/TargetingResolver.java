package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.port.*;
import java.util.Optional;

public final class TargetingResolver {

    public TargetResult resolve(TargetingRequest request, TargetingPort port) {
        if (request == null || port == null) throw new IllegalArgumentException();

        ActorId caster = request.casterId();
        TargetingSpec spec = request.spec();
        Optional<ViewRay> rayOpt = port.viewRay(caster);

        if (spec.mode() == TargetingMode.RAYCAST_SINGLE && rayOpt.isPresent()) {
            ViewRay ray = rayOpt.get();
            Optional<RaycastHit> hitOpt = port.raycastFirstHit(caster, ray, spec.rangeBlocks());
            if (hitOpt.isPresent()) {
                TargetResult direct = directResult(caster, toTarget(hitOpt.get()));
                if (direct.actorTargetOpt().isPresent()) return direct;
                if (spec.fallbackPolicy() == TargetingFallback.SELF) return TargetResult.fallback(new ActorTarget(caster), TargetingFallback.SELF, caster);
                return direct;
            }
        }

        return fallbackResult(caster, spec, rayOpt.orElse(null));
    }

    private static Target toTarget(RaycastHit hit) {
        return hit instanceof RaycastEntityHit eh ? new ActorTarget(eh.actorId()) : new PointTarget(hit.hitPosition());
    }

    private static TargetResult directResult(ActorId caster, Target target) {
        return target instanceof ActorTarget at && !at.actorId().equals(caster) ? TargetResult.direct(target, at.actorId()) : TargetResult.direct(target, null);
    }

    private static TargetResult fallbackResult(ActorId caster, TargetingSpec spec, ViewRay ray) {
        TargetingFallback policy = spec.fallbackPolicy();
        if (policy == TargetingFallback.SELF) return TargetResult.fallback(new ActorTarget(caster), policy, caster);
        if (policy == TargetingFallback.IMAGINARY_POINT && ray != null) return TargetResult.fallback(new PointTarget(ray.pointAt(spec.rangeBlocks() * 0.5)), policy, null);
        return TargetResult.fallback(new ActorTarget(caster), TargetingFallback.SELF, caster);
    }
}