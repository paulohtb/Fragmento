package com.pgalaxyp.fragmento.rpg.platform.impl.events;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.VirtualTarget;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;
import com.pgalaxyp.fragmento.rpg.core.rule.action.PendingTargeting;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import com.pgalaxyp.fragmento.rpg.platform.api.world.TargetingQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldTarget;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;

public final class WorldQueryAdapter implements WorldQueryGateway {

    private final WorldView world;

    public WorldQueryAdapter(WorldView world) {
        this.world = world;
    }

    @Override
    public TargetingResolution resolveTargeting(PendingTargeting request) {
        var vectors = world.casterVectors(request.actorId());

        var casterPos = new Vec3(
                vectors.pos().x(),
                vectors.pos().y(),
                vectors.pos().z()
        );

        var casterDir = new Vec3(
                vectors.look().x(),
                vectors.look().y(),
                vectors.look().z()
        ).normalized();

        var hit = world.raycastLivingEntity(
                new TargetingQuery(
                        request.actorId(),
                        request.request(),
                        request.maxEntityDistance(),
                        request.maxVirtualDistance()
                )
        );

        if (hit.isPresent()) {
            var h = hit.get();
            return new TargetingResolution(
                    request.actorId(),
                    casterPos,
                    casterDir,
                    new WorldTarget(
                            h.target().position(),
                            h.target().bounds(),
                            h.target().actorIdOrZero()
                    )
            );
        }

        var virtualPos = casterPos.add(casterDir.mul(request.maxVirtualDistance()));
        return new TargetingResolution(
                request.actorId(),
                casterPos,
                casterDir,
                new VirtualTarget(virtualPos)
        );
    }
}