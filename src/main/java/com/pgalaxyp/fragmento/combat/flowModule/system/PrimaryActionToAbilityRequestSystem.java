package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorSyncView;
import com.pgalaxyp.fragmento.combat.intentModule.api.PrimaryActionIntent;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityPrimaryRequested;
import java.util.Objects;

public record PrimaryActionToAbilityRequestSystem() implements FrameSystem {
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var sync = bus.viewOpt(ActorSyncView.class).orElse(ActorSyncView.EMPTY);
        var live = sync.liveActorIds();
        for (var env : bus.intents(com.pgalaxyp.fragmento.combat.intentModule.api.IntentEnvelope.class)) {
            if (env.intent() instanceof PrimaryActionIntent(var weaponId) && live.contains(env.actorId())) bus.publish(new AbilityPrimaryRequested(env.actorId(), weaponId));
        }
    }
}