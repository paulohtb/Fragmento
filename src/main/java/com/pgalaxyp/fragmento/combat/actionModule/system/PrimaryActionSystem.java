package com.pgalaxyp.fragmento.combat.actionModule.system;

import com.pgalaxyp.fragmento.combat.classModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actionModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.LiveActorsView;
import com.pgalaxyp.fragmento.combat.actionModule.event.ActionRejected;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityStartCommand;
import java.util.*;

public record PrimaryActionSystem(Map<ClassId, ClassKit> classKits) implements FrameSystem {
    public PrimaryActionSystem { classKits = Map.copyOf(Objects.requireNonNull(classKits)); }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        var live = bus.viewOpt(LiveActorsView.class).orElse(LiveActorsView.empty());
        var classes = bus.viewOpt(ActorClassView.class).orElse(ActorClassView.empty());
        for (var cmd : bus.commands(PrimaryActionCommand.class)) {
            if (!live.contains(cmd.actorId())) continue;
            var classId = classes.classIdOf(cmd.actorId()).orElse(null);
            if (classId == null) { bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.NO_CLASS)); continue; }
            var kit = classKits.get(classId);
            if (kit == null) { bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.NO_CLASS_KIT)); continue; }
            if (!kit.weapons().contains(cmd.weaponId())) { bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.WEAPON_NOT_ALLOWED)); continue; }
            var abilityId = kit.defaultLoadout().get(ActionSlot.PRIMARY);
            if (abilityId == null) { bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.NO_ABILITY_BOUND)); continue; }
            bus.command(new AbilityStartCommand(cmd.actorId(), abilityId));
        }
    }
}