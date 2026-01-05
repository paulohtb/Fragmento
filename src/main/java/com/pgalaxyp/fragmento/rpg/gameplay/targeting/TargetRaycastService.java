package com.pgalaxyp.fragmento.rpg.gameplay.targeting;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;
import com.pgalaxyp.fragmento.rpg.platform.api.world.RaycastQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;

public final class TargetRaycastService {

    private final WorldView world;
    private final ActorRepository actors;

    public TargetRaycastService(WorldView world, ActorRepository actors) {
        this.world = Objects.requireNonNull(world);
        this.actors = Objects.requireNonNull(actors);
    }

    public Target resolve(PlayerView player) {
        var origin = player.position();
        var dir = player.lookDirection().normalized();

        var hit = world.raycastLivingEntity(new RaycastQuery(origin, dir, 3.0, 20.0));
        if (hit.isPresent()) {
            var h = hit.get();
            var st = actors.findState(h.actorId());
            if (st.isPresent()) {
                return new EntityTarget(h.actorId(), st.get().bounds().center(), st.get().bounds());
            }
        }

        return new VirtualTarget(origin.add(dir.mul(10.0)));
    }

    public static Aabb defaultBoundsAt(Vec3 p) {
        var r = 0.3;
        var h = 1.8;
        return new Aabb(
                new Vec3(p.x() - r, p.y(), p.z() - r),
                new Vec3(p.x() + r, p.y() + h, p.z() + r)
        );
    }
}