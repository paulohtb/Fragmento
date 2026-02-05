package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.*;

public record ActorHealthFromDamageSystem(ActorHealthPort health) implements FrameSystem {
    public ActorHealthFromDamageSystem { Objects.requireNonNull(health); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var dmg = bus.events(DamageApplied.class);
        if (dmg.isEmpty()) return;
        var base = bus.viewOpt(ActorView.class).orElse(ActorView.empty());
        var deltas = new ArrayList<HealthDelta>(dmg.size());
        for (var d : dmg) deltas.add(new HealthDelta(d.targetActorId(), Math.negateExact(d.damageHearts())));
        bus.view(ActorView.class, health.apply(base, deltas));
    }
}
