package com.pgalaxyp.fragmento.combat.transport.snapshot.api;

import com.pgalaxyp.fragmento.combat.ability.model.*;
import java.util.*;

public record ActorExecutionState(ActorExecutionPhase phase, AbilityInstanceView ability) {
    public ActorExecutionState {
        Objects.requireNonNull(phase);
        if (phase == ActorExecutionPhase.IDLE && ability != null) throw new IllegalArgumentException();
        if (phase != ActorExecutionPhase.IDLE && ability == null) throw new IllegalArgumentException();
    }
}