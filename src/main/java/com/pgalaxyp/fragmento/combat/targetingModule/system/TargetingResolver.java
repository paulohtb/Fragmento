package com.pgalaxyp.fragmento.combat.targetingModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.util.Vec3d;
import com.pgalaxyp.fragmento.combat.util.ViewRay;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.port.*;
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
                RaycastHit hit = hitOpt.get();
                if (hit instanceof RaycastEntityHit eh) {
                    return TargetResult.direct(new ActorTarget(eh.actorId()), eh.actorId());
                }
                return TargetResult.direct(new PointTarget(hit.hitPosition()), null);
            }

            return imaginary(ray, spec);
        }

        return imaginary(rayOpt.orElse(null), spec);
    }

    private static TargetResult imaginary(ViewRay ray, TargetingSpec spec) {
        if (ray == null) {
            return TargetResult.fallback(
                    new PointTarget(new Vec3d(0.0, 0.0, 0.0)),
                    TargetingFallback.IMAGINARY_POINT,
                    null
            );
        }

        return TargetResult.fallback(
                new PointTarget(ray.pointAt(spec.rangeBlocks() * 0.5)),
                TargetingFallback.IMAGINARY_POINT,
                null
        );
    }
}