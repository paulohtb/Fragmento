package com.pgalaxyp.fragmento.rpg.gameplay.targeting;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;

public final class TargetRaycastService {

    private final ActorRepository actors;
    private final WorldView world;

    public TargetRaycastService(ActorRepository actors, WorldView world) {
        this.actors = Objects.requireNonNull(actors);
        this.world = Objects.requireNonNull(world);
    }

    public Target resolve(long actorId) {
        var stSelf = actors.findState(actorId).orElse(null);
        if (stSelf == null || !stSelf.alive()) {
            return new VirtualTarget(new Vec3(0, 0, 0));
        }

        var origin = stSelf.position();
        var dir = stSelf.lookDirection().normalized();

        var hit = world.raycastLivingEntity(actorId, origin, dir, 3.0, 20.0).orElse(null);

        if (hit != null) {
            var st = actors.findState(hit.actorId()).orElse(null);
            if (st != null && st.alive()) {
                return new EntityTarget(hit.actorId(), st.bounds().center(), st.bounds());
            }
        }

        return new VirtualTarget(origin.add(dir.mul(10.0)));
    }

    public static Aabb defaultBoundsAt(Vec3 p) {
        var e = 0.3;
        return new Aabb(
                new Vec3(p.x() - e, p.y(), p.z() - e),
                new Vec3(p.x() + e, p.y() + 1.8, p.z() + e)
        );
    }
}