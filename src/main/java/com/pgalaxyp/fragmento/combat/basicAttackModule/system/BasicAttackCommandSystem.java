package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackCommand;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameBus;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Objects;

final class BasicAttackCommandSystem implements FrameSystem {
    private final BasicAttackEngine engine;

    BasicAttackCommandSystem(BasicAttackEngine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        for (var cmd : bus.commands(BasicAttackCommand.class)) {
            bus.publish(engine.tryTrigger(cmd.actorId(), cmd.weaponId(), frame.frameId()));
        }
    }
}
