package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityStarted;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import com.pgalaxyp.fragmento.combat.random.*;
import java.util.Objects;

public final class AbilityStartEffectDispatch implements FrameSystem {

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (AbilityStarted e : bus.events(AbilityStarted.class)) {
            e.targeting().actorTargetOpt().ifPresent(target -> bus.publish(new EffectTriggered(e.startEffect(), e.source(), target)));
        }
    }
}