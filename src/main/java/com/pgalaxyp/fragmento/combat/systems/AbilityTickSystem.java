package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityEvent;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.ability.*;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.Objects;

public final class AbilityTickSystem implements FrameSystem {
    private final AbilityCombatPort abilities;

    public AbilityTickSystem(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        var r = abilities.tick(frame, state);

        for (var ev : r.events()) {
            switch (ev) {
                case AbilityEvent.Started s ->
                        bus.publish(new AbilityStarted(s.snapshot().actorId(), s.snapshot(), s.startEffect(), s.targeting(), s.source(), s.target()));
                case AbilityEvent.Ended e ->
                        bus.publish(new AbilityEnded(e.snapshot().actorId(), e.snapshot()));
                case AbilityEvent.Rejected __ -> {
                }
            }
        }

        r.deltas().forEach(bus::emit);
    }
}