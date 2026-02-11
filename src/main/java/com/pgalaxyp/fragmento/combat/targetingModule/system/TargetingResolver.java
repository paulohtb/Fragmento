package com.pgalaxyp.fragmento.combat.targetingModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.targetingModule.api.ActorTarget;
import com.pgalaxyp.fragmento.combat.targetingModule.api.PointTarget;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetResult;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingMode;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingRequest;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import com.pgalaxyp.fragmento.combat.targetingModule.port.RaycastBlockHit;
import com.pgalaxyp.fragmento.combat.targetingModule.port.RaycastEntityHit;
import com.pgalaxyp.fragmento.combat.targetingModule.port.RaycastHit;
import com.pgalaxyp.fragmento.combat.targetingModule.port.TargetingPort;
import com.pgalaxyp.fragmento.combat.util.Vec3d;
import com.pgalaxyp.fragmento.combat.util.ViewRay;
import java.util.Optional;

final class TargetingResolver {
    TargetResult resolve(TargetingRequest request, TargetingPort port) {
        if (request == null || port == null) throw new IllegalArgumentException();

        ActorId caster = request.casterId();
        TargetingSpec spec = request.spec();
        Optional<ViewRay> rayOpt = port.viewRay(caster);

        if (spec.mode() == TargetingMode.RAYCAST_SINGLE && rayOpt.isPresent()) {
            ViewRay ray = rayOpt.get();
            Optional<RaycastHit> hitOpt = port.raycastFirstHit(caster, ray, spec.maxRangeBlocks());
            if (hitOpt.isPresent()) {
                RaycastHit hit = hitOpt.get();
                if (hit.distance() >= spec.minRangeBlocks() && hit.distance() <= spec.maxRangeBlocks()) {
                    if (hit instanceof RaycastEntityHit eh) return TargetResult.direct(new ActorTarget(eh.actorId()));
                    if (hit instanceof RaycastBlockHit bh) return TargetResult.direct(new PointTarget(bh.hitPosition()));
                    return TargetResult.direct(new PointTarget(hit.hitPosition()));
                }
            }
            return fallback(ray, spec);
        }

        return fallback(rayOpt.orElse(null), spec);
    }

    private static TargetResult fallback(ViewRay ray, TargetingSpec spec) {
        return ray == null
                ? TargetResult.fallback(new PointTarget(new Vec3d(0.0, 0.0, 0.0)), spec.fallbackPolicy())
                : TargetResult.fallback(new PointTarget(ray.pointAt(spec.fallbackDistanceBlocks())), spec.fallbackPolicy());
    }
}
