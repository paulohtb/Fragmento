package com.pgalaxyp.fragmento.rpg.engine.input;

public enum IntentType {
    PRIMARY_ACTION,
    INTERRUPT;

    public boolean isPrimary() {
        return this == PRIMARY_ACTION;
    }
}