package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

public enum SpiritAnimation {
    NONE(0),
    SPAWN(1),
    TRAVEL_SHORT(2),
    TRAVEL_MEDIUM(3),
    TRAVEL_LONG(4),
    DESPAWN(5);

    public final int id;

    SpiritAnimation(int id) {
        this.id = id;
    }

    public static SpiritAnimation fromId(int id) {
        for (SpiritAnimation a : values()) {
            if (a.id == id) return a;
        }
        return NONE;
    }
}
