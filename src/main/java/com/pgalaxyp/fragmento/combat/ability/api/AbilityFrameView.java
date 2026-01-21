package com.pgalaxyp.fragmento.combat.ability.api;

import java.util.*;

public record AbilityFrameView(List<AbilitySnapshot> active) {
    public AbilityFrameView {
        active = List.copyOf(Objects.requireNonNull(active));
    }
}