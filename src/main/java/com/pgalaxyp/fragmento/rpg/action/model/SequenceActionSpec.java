package com.pgalaxyp.fragmento.rpg.action.model;

import java.util.*;

public record SequenceActionSpec(List<ActionEffectRef> effects) implements ActionSpec {

    public SequenceActionSpec {
        Objects.requireNonNull(effects);
        if (effects.isEmpty()) {
            throw new IllegalArgumentException();
        }

        effects = List.copyOf(effects);
    }
}