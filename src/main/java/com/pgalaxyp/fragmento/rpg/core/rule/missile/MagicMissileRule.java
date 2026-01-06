package com.pgalaxyp.fragmento.rpg.core.rule.missile;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.domain.event.MissileExpired;
import com.pgalaxyp.fragmento.rpg.core.state.missile.MagicMissileState;
import java.util.ArrayList;
import java.util.List;

public final class MagicMissileRule {

    public Result tick(MagicMissileState missile, double deltaSeconds) {
        var next = missile.tick(deltaSeconds);

        if (!next.expired()) {
            return new Result(next, List.of());
        }

        var events = new ArrayList<>();
        events.add(new DamageApplied(
                missile.sourceActorId(),
                missile.targetActorId(),
                missile.damage()
        ));
        events.add(new MissileExpired(missile.id()));

        return new Result(null, events);
    }

    public record Result(
            MagicMissileState nextState,
            List<Object> events
    ) {}
}