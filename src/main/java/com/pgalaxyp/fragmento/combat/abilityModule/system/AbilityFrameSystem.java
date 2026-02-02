package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import com.pgalaxyp.fragmento.combat.intentModule.api.PrimaryActionIntent;
import java.util.*;

public final class AbilityFrameSystem implements FrameSystem {
    private final AbilityActorIndex actors = new AbilityActorIndex();
    private final AbilityPort abilities;

    public AbilityFrameSystem(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<com.pgalaxyp.fragmento.combat.weaponModule.WeaponId, AbilityId> primaryByWeapon) {
        abilities = new AbilityEngine(defs, rules, primaryByWeapon, actors);
    }

    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var sync = bus.viewOpt(AbilityActorSyncView.class).orElse(AbilityActorSyncView.EMPTY);
        actors.update(sync.classes());
        var live = sync.liveActorIds();
        for (var env : bus.intents(IntentEnvelope.class)) {
            var actorId = env.actorId();
            if (!live.contains(actorId)) continue;
            var intent = env.intent();
            if (intent instanceof PrimaryActionIntent(var weaponId)) {
                abilities.tryExecutePrimary(actorId, weaponId, frame).events().forEach(bus::publish);
            }
        }
        abilities.tick(frame, live).events().forEach(bus::publish);
        bus.view(AbilityViewSnapshot.class, abilities.view(live, frame.frameId()));
    }
}