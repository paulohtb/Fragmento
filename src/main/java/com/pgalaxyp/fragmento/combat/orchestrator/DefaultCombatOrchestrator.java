package com.pgalaxyp.fragmento.combat.orchestrator;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityCombatPort;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityCombatResult;
import com.pgalaxyp.fragmento.combat.events.ability.*;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.effect.EffectTriggered;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetResult;
import com.pgalaxyp.fragmento.combat.core.ids.EffectId;
import java.util.*;

public final class DefaultCombatOrchestrator implements CombatOrchestrator {
    private final AbilityCombatPort abilities;

    public DefaultCombatOrchestrator(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        List<AbilityResolved> resolvedList = bus.events(AbilityResolved.class);

        for (AbilityResolved resolved : resolvedList) {
            AbilityIntent intent = new AbilityIntent(resolved.actorId(), resolved.abilityId());
            AbilityCombatResult result = abilities.tryExecute(intent, frame, state);
            publishAbilityEvents(bus, result.events());
            emitDeltas(bus, result.deltas());
        }

        AbilityCombatResult tickResult = abilities.tick(frame, state);
        publishAbilityEvents(bus, tickResult.events());
        emitDeltas(bus, tickResult.deltas());
    }

    private static void publishAbilityEvents(FrameBus bus, List<AbilityEvent> events) {
        for (AbilityEvent event : events) {
            if (event instanceof AbilityEvent.Started(AbilitySnapshot snapshot, EffectId startEffect, TargetResult targeting, ActorId source, ActorId target)) {
                bus.publish(new AbilityStarted(snapshot.actorId(), snapshot, startEffect, targeting, source, target));
                bus.publish(new EffectTriggered(startEffect, source, target));
                continue;
            }
            if (event instanceof AbilityEvent.Ended(AbilitySnapshot snapshot)) {
                bus.publish(new AbilityEnded(snapshot.actorId(), snapshot));
                continue;
            }
            if (event instanceof AbilityEvent.Rejected(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason)) {
                bus.publish(new AbilityRejected(actorId, abilityId, reason));
            }
        }
    }

    private static void emitDeltas(FrameBus bus, List<StateDelta> deltas) {
        for (StateDelta delta : deltas) { bus.emit(delta); }
    }
}