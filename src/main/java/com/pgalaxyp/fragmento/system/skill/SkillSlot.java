package com.pgalaxyp.fragmento.system.skill;

public enum SkillSlot {

    BASIC(0),
    SPECIAL(1),
    ULTIMATE(2);

    private final int id;

    SkillSlot(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public static SkillSlot fromId(int id) {
        for (SkillSlot slot : values()) {
            if (slot.id == id) return slot;
        }
        return null;
    }
}
