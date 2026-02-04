package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import java.util.Objects;

public record PrimaryActionAbilitySystem(AbilityPort abilities) implements FrameSystem {
    public PrimaryActionAbilitySystem { Objects.requireNonNull(abilities); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var actors = bus.viewOpt(ActorView.class).orElse(ActorView.empty());
        var live = actors.ids();
        for (var env : bus.intents(IntentEnvelope.class)) {
            if (!(env.intent() instanceof PrimaryActionIntent(var weaponId))) continue;
            if (!live.contains(env.actorId())) continue;
            actors.findActor(env.actorId()).ifPresent(st -> abilities.tryExecutePrimary(env.actorId(), weaponId, st.classId(), frame).events().forEach(bus::publish));
        }
        abilities.tick(frame, live).events().forEach(bus::publish);
        bus.view(AbilityViewSnapshot.class, abilities.view(live, frame.frameId()));
    }
}