package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.ability.*;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.Objects;

public final class AbilityExecutionSystem implements FrameSystem {
    private final AbilityCombatPort abilities;

    public AbilityExecutionSystem(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (AbilityRequested req : bus.events(AbilityRequested.class)) {
            var r = abilities.tryExecute(new AbilityIntent(req.actorId(), req.abilityId()), frame, state);
            for (var ev : r.events()) publish(bus, req.actorId(), ev);
            r.deltas().forEach(bus::emit);
        }
    }

    private static void publish(FrameBus bus, com.pgalaxyp.fragmento.combat.core.ids.ActorId actorId, AbilityEvent ev) {
        switch (ev) {
            case AbilityEvent.Started s ->
                    bus.publish(new AbilityStarted(actorId, s.snapshot(), s.startEffect(), s.targeting(), s.source(), s.target()));
            case AbilityEvent.Ended e ->
                    bus.publish(new AbilityEnded(actorId, e.snapshot()));
            case AbilityEvent.Rejected __ -> {
            }
        }
    }
}
