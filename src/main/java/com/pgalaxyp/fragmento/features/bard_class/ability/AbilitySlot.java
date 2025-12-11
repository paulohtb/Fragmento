package com.pgalaxyp.fragmento.features.bard_class.ability;

public enum AbilitySlot {

    BASIC(0),
    SPECIAL(1),
    ULTIMATE(2);

    private final int id;

    AbilitySlot(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public static AbilitySlot fromId(int id) {
        for (AbilitySlot slot : values()) {
            if (slot.id == id) {
                return slot;
            }
        }
        return null;
    }
}
