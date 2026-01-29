package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.abilityModule.intent.*;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.*;

public final class AbilityFrameSystem implements FrameSystem {
    private final AbilityActorIndex actors = new AbilityActorIndex();
    private final AbilityPort abilities;

    public AbilityFrameSystem(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon) {
        abilities = new AbilityEngine(defs, rules, primaryByWeapon, actors);
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var sync = bus.viewOpt(ActorSyncView.class).orElse(null);
        actors.update(sync == null ? Map.of() : sync.classes());
        Collection<ActorId> live = sync == null ? Set.of() : sync.liveActorIds();
        for (var env : bus.intents(IntentEnvelope.class)) {
            var actorId = env.actorId();
            var intent = env.intent();
            if (intent instanceof AbilityUseIntent(var abilityId, var weaponId)) abilities.tryExecute(actorId, abilityId, weaponId, frame).events().forEach(bus::publish);
            else if (intent instanceof AbilityPrimaryIntent(var weaponId)) abilities.tryExecutePrimary(actorId, weaponId, frame).events().forEach(bus::publish);
        }
        abilities.tick(frame, live).events().forEach(bus::publish);
        bus.view(AbilityViewSnapshot.class, abilities.view(live, frame.frameId()));
    }
}