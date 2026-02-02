package com.pgalaxyp.fragmento.combat.engineModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorSyncView;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityActorSyncView;
import java.util.Objects;

public record ActorToAbilitySyncSystem() implements FrameSystem {
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        ActorSyncView sync = bus.viewOpt(ActorSyncView.class).orElse(null);
        bus.view(AbilityActorSyncView.class, sync == null ? AbilityActorSyncView.EMPTY : new AbilityActorSyncView(sync.classes(), sync.liveActorIds()));
    }
}