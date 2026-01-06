package com.pgalaxyp.fragmento.rpg.core.domain.action;

import java.util.EnumSet;

public enum InterruptMask {
    NONE,
    MOVEMENT,
    DAMAGE,
    CONTROL;

    public static EnumSet<InterruptMask> none() {
        return EnumSet.of(NONE);
    }
}