package com.pgalaxyp.fragmento.rpg.action.model;

import java.util.*;

public record SingleActionSpec(ActionEffectRef effect) implements ActionSpec {

    public SingleActionSpec {
        Objects.requireNonNull(effect);
    }
}