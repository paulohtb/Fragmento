package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityPrimaryRequested;
import java.util.*;

public final class AbilityFrameSystem implements FrameSystem {
    private final AbilityPort abilities;

    public AbilityFrameSystem(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon, AbilityTuning tuning) {
        this.abilities = AbilityModule.create(defs, rules, primaryByWeapon, tuning);
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        ActorSyncView sync = bus.viewOpt(ActorSyncView.class).orElse(ActorSyncView.EMPTY);
        Set<ActorId> live = sync.liveActorIds();
        for (var req : bus.events(AbilityPrimaryRequested.class)) {
            ActorId actorId = req.actorId();
            if (!live.contains(actorId)) continue;
            ClassId classId = sync.classIdOf(actorId).orElse(null);
            if (classId == null) continue;
            abilities.tryExecutePrimary(actorId, req.weaponId(), classId, frame).events().forEach(bus::publish);
        }
        abilities.tick(frame, live).events().forEach(bus::publish);
        bus.view(AbilityViewSnapshot.class, abilities.view(live, frame.frameId()));
    }
}