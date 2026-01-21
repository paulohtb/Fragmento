package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.Objects;

public record ComboSnapshot(
        ActorId actorId,
        ComboId comboId,
        int stepIndex,
        int stepsTotal,
        long startedAtFrame,
        long lastAcceptedFrame,
        long expiresAtFrameExclusive
) {

    public ComboSnapshot {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);

        if (stepIndex < 0) throw new IllegalArgumentException();
        if (stepsTotal <= 0) throw new IllegalArgumentException();
        if (stepIndex >= stepsTotal) throw new IllegalArgumentException();
        if (startedAtFrame < 0) throw new IllegalArgumentException();
        if (lastAcceptedFrame < startedAtFrame) throw new IllegalArgumentException();
        if (expiresAtFrameExclusive <= lastAcceptedFrame) throw new IllegalArgumentException();
    }
}