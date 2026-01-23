package com.pgalaxyp.fragmento.combat.inputModule.api;

public record InputDecision(boolean consumeVanilla) {
    public static InputDecision passThrough() { return new InputDecision(false); }
}