package com.pgalaxyp.fragmento.rpg.gameplay.effects.missile;

import com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.Target;
import java.util.Optional;

public final class MissileGuidance {
    public Vec3 aimPoint(Target target, ActorRepository actors) {
        if (target.isReal()) {
            var id = target.actorIdOrZero();
            var st = actors.findState(id);
            if (st.isPresent() && st.get().alive()) return st.get().bounds().center();
        }
        return target.position();
    }

    public Optional<Vec3> lastKnownIfReal(Target target, ActorRepository actors) {
        if (!target.isReal()) return Optional.empty();
        var id = target.actorIdOrZero();
        var st = actors.findState(id);
        if (st.isEmpty()) return Optional.empty();
        if (!st.get().alive()) return Optional.empty();
        return Optional.of(st.get().bounds().center());
    }
}