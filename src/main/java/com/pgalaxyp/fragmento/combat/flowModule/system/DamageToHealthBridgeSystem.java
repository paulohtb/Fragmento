package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.actorModule.event.ActorHealthAdjusted;
import java.util.Objects;

public record DamageToHealthBridgeSystem() implements FrameSystem {
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var d : bus.events(DamageApplied.class)) bus.publish(new ActorHealthAdjusted(d.targetActorId(), Math.negateExact(d.damageHearts())));
    }
}