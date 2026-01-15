package com.pgalaxyp.fragmento.combat.input.api;

public record InputDecision(boolean consumeVanilla) {
    public static InputDecision passThrough() { return new InputDecision(false); }
}