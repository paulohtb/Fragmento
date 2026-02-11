package com.pgalaxyp.fragmento.combat.actionModule.system;

import com.pgalaxyp.fragmento.combat.actionModule.api.ActionRejectReason;
import com.pgalaxyp.fragmento.combat.actionModule.api.ActionSlot;
import com.pgalaxyp.fragmento.combat.actionModule.api.PrimaryActionCommand;
import com.pgalaxyp.fragmento.combat.actionModule.event.ActionRejected;
import com.pgalaxyp.fragmento.combat.actorModule.api.LiveActorsView;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackCommand;
import com.pgalaxyp.fragmento.combat.classModule.api.ActorClassView;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameBus;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Map;
import java.util.Objects;

public record PrimaryActionSystem(Map<ClassId, ClassKit> classKits) implements FrameSystem {
    public PrimaryActionSystem {
        classKits = Map.copyOf(Objects.requireNonNull(classKits));
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);

        var live = bus.viewOpt(LiveActorsView.class).orElse(LiveActorsView.empty());
        var classes = bus.viewOpt(ActorClassView.class).orElse(ActorClassView.empty());

        for (var cmd : bus.commands(PrimaryActionCommand.class)) {
            if (!live.contains(cmd.actorId())) continue;

            var classId = classes.classIdOf(cmd.actorId()).orElse(null);
            if (classId == null) {
                bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.NO_CLASS));
                continue;
            }

            var kit = classKits.get(classId);
            if (kit == null) {
                bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.NO_CLASS_KIT));
                continue;
            }

            if (!kit.weapons().contains(cmd.weaponId())) {
                bus.publish(new ActionRejected(cmd.actorId(), ActionSlot.PRIMARY, cmd.weaponId(), ActionRejectReason.WEAPON_NOT_ALLOWED));
                continue;
            }

            bus.command(new BasicAttackCommand(cmd.actorId(), cmd.weaponId()));
        }
    }
}
