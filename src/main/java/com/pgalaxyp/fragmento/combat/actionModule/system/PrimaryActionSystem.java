package com.pgalaxyp.fragmento.combat.actionModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityStartRequest;
import com.pgalaxyp.fragmento.combat.actionModule.api.*;
import com.pgalaxyp.fragmento.combat.actionModule.event.ActionRejected;
import com.pgalaxyp.fragmento.combat.classModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import java.util.*;

public record PrimaryActionSystem(Map<ClassId, ClassKit> classKits) implements FrameSystem {
    public PrimaryActionSystem { classKits = Map.copyOf(Objects.requireNonNull(classKits)); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var actors = bus.viewOpt(ActorView.class).orElse(ActorView.empty());
        for (var env : bus.intents(IntentEnvelope.class)) {
            if (!(env.intent() instanceof PrimaryActionIntent(var weaponId))) continue;
            var st = actors.findActor(env.actorId()).orElse(null);
            if (st == null) continue;
            var kit = classKits.get(st.classId());
            if (kit == null) { bus.publish(new ActionRejected(env.actorId(), ActionSlot.PRIMARY, weaponId, ActionRejectReason.NO_CLASS_KIT)); continue; }
            if (!kit.weapons().contains(weaponId)) { bus.publish(new ActionRejected(env.actorId(), ActionSlot.PRIMARY, weaponId, ActionRejectReason.WEAPON_NOT_ALLOWED)); continue; }
            var abilityId = kit.defaultLoadout().get(ActionSlot.PRIMARY);
            if (abilityId == null) { bus.publish(new ActionRejected(env.actorId(), ActionSlot.PRIMARY, weaponId, ActionRejectReason.NO_ABILITY_BOUND)); continue; }
            bus.publish(new AbilityStartRequest(env.actorId(), abilityId));
        }
    }
}
