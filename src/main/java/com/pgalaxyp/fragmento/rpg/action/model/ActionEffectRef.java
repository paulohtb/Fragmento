package com.pgalaxyp.fragmento.rpg.action.model;

import java.util.*;

public record ActionEffectRef(String effectKey) {

    public ActionEffectRef {
        Objects.requireNonNull(effectKey);
        if (effectKey.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}