package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.system.ComboEngine;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.combo.*;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.Objects;

public final class ComboExecutionSystem implements FrameSystem {
    private final ComboEngine combos;
    private final GameContent content;

    public ComboExecutionSystem(ComboEngine combos, GameContent content) {
        this.combos = Objects.requireNonNull(combos);
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (ComboInputReceived in : bus.events(ComboInputReceived.class)) {
            var weapon = state.actor(in.actorId()).equippedWeaponId().orElse(null);
            if (weapon == null) continue;

            var baseOpt = content.combos().baseFor(weapon);
            if (baseOpt.isEmpty()) continue;

            var base = baseOpt.get();
            if (!base.comboId().equals(in.comboId())) continue;

            ComboOutcome oc = combos.tryInput(new ComboIntent(in.actorId(), in.comboId(), in.input()), frame, base.pattern());
            for (ComboEvent ev : oc.events()) publish(bus, in.actorId(), ev);
        }
    }

    private static void publish(FrameBus bus, com.pgalaxyp.fragmento.combat.core.ids.ActorId actorId, ComboEvent ev) {
        switch (ev) {
            case ComboEvent.Started s -> bus.publish(new ComboStarted(actorId, s.snapshot(), s.input()));
            case ComboEvent.Advanced a -> bus.publish(new ComboAdvanced(actorId, a.snapshot(), a.input()));
            case ComboEvent.Completed c -> bus.publish(new ComboCompleted(actorId, c.snapshot(), c.input()));
            case ComboEvent.Reset r -> bus.publish(new ComboReset(actorId, r.comboId(), r.reason()));
            case ComboEvent.Rejected __ -> {
            }
        }
    }
}