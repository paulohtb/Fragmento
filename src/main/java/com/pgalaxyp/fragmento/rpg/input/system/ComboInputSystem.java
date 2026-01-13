package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import java.util.*;

public final class ComboInputSystem {

    public Optional<DomainIntent> onPrimaryAction(InputContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        var actorId = context.actorId();
        if (actorId == null) {
            return Optional.empty();
        }

        var snapshot = context.snapshot();
        var stateOpt = snapshot.findActor(actorId);
        if (stateOpt.isEmpty()) {
            return Optional.empty();
        }

        ActorState state = stateOpt.get();
        var comboOpt = state.combo();
        if (comboOpt.isPresent()) {
            var combo = comboOpt.get();
            int next = combo.stepIndex() + 1;
            if (next >= combo.stepsTotal()) {
                return Optional.empty();
            }
            return Optional.of(new PerformActionIntent(next));
        }

        if (state.equippedWeaponId().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new PerformActionIntent(0));
    }
}