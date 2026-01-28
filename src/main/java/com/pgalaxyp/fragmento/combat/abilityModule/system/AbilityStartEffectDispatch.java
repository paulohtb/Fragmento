package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityStarted;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import java.util.Objects;

public final class AbilityStartEffectDispatch implements FrameSystem {
    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        GameState gs = (GameState) state;
        for (AbilityStarted e : bus.events(AbilityStarted.class)) {
            e.targeting().actorTargetOpt().ifPresent(target -> bus.publish(new EffectTriggered(e.startEffect(), e.source(), target)));
        }
    }
}