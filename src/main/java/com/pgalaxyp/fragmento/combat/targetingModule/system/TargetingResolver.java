package com.pgalaxyp.fragmento.combat.targetingModule.system;

import com.pgalaxyp.fragmento.combat.util.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Optional;

final class TargetingResolver {
    TargetResult resolve(TargetingRequest request, TargetingPort port) {
        if (request == null || port == null) throw new IllegalArgumentException();
        ActorId caster = request.casterId();
        TargetingSpec spec = request.spec();
        Optional<ViewRay> rayOpt = port.viewRay(caster);
        if (spec.mode() == TargetingMode.RAYCAST_SINGLE && rayOpt.isPresent()) {
            ViewRay ray = rayOpt.get();
            Optional<RaycastHit> hitOpt = port.raycastFirstHit(caster, ray, spec.rangeBlocks());
            if (hitOpt.isPresent()) {
                RaycastHit hit = hitOpt.get();
                return hit instanceof RaycastEntityHit eh ? TargetResult.direct(new ActorTarget(eh.actorId())) : TargetResult.direct(new PointTarget(hit.hitPosition()));
            }
            return imaginary(ray, spec);
        }
        return imaginary(rayOpt.orElse(null), spec);
    }

    private static TargetResult imaginary(ViewRay ray, TargetingSpec spec) {
        return ray == null
                ? TargetResult.fallback(new PointTarget(new Vec3d(0.0, 0.0, 0.0)), spec.fallbackPolicy())
                : TargetResult.fallback(new PointTarget(ray.pointAt(spec.rangeBlocks() * 0.5)), spec.fallbackPolicy());
    }
}