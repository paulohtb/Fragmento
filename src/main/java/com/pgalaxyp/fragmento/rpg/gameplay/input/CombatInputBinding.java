package com.pgalaxyp.fragmento.rpg.gameplay.input;

import java.util.EnumSet;
import java.util.Set;

public record CombatInputBinding(Set<InputAction> actions) {
    public CombatInputBinding {
        actions = Set.copyOf(actions);
    }

    public static CombatInputBinding defaultBinding() {
        return new CombatInputBinding(EnumSet.of(InputAction.ATTACK_PRIMARY));
    }

    public String actionId(InputAction action) {
        return action.name();
    }
}