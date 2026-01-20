package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.combo.api.ComboEvent;
import com.pgalaxyp.fragmento.combat.combo.system.ComboEngine;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.combo.ComboReset;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.Objects;

public final class ComboTickSystem implements FrameSystem {
    private final ComboEngine combos;

    public ComboTickSystem(ComboEngine combos) {
        this.combos = Objects.requireNonNull(combos);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        var tr = combos.tick(frame);
        for (var ev : tr.events()) {
            if (ev instanceof ComboEvent.Reset r) {
                bus.publish(new ComboReset(r.actorId(), r.comboId(), r.reason()));
            }
        }
    }
}