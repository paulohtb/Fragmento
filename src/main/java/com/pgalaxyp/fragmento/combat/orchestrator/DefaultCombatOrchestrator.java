package com.pgalaxyp.fragmento.combat.orchestrator;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.ability.event.*;
import com.pgalaxyp.fragmento.combat.ability.port.AbilityCombatPort;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.effect.api.EffectId;
import com.pgalaxyp.fragmento.combat.effect.event.EffectTriggered;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetResult;

import java.util.Objects;

public final class DefaultCombatOrchestrator implements CombatOrchestrator {
    private final AbilityCombatPort abilities;

    public DefaultCombatOrchestrator(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (AbilityResolved r : bus.events(AbilityResolved.class)) {
            var result = abilities.tryExecute(new AbilityIntent(r.actorId(), r.abilityId()), frame, state);
            result.events().forEach(e -> publish(bus, e));
        }

        abilities.tick(frame, state).events().forEach(e -> publish(bus, e));
    }

    private static void publish(FrameBus bus, AbilityEvent event) {
        if (event instanceof AbilityEvent.Started(AbilitySnapshot snapshot1, EffectId startEffect, TargetResult targeting, ActorId source, ActorId target)) {
            bus.publish(new EffectTriggered(startEffect, source, target));
            return;
        }
        if (event instanceof AbilityEvent.Ended(com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot snapshot)) {
            return;
        }
        AbilityEvent.Rejected rejected = (AbilityEvent.Rejected) event;
        ActorId actorId = rejected.actorId();
        AbilityId abilityId = rejected.abilityId();
        AbilityRejectReason reason = rejected.reason();
    }
}