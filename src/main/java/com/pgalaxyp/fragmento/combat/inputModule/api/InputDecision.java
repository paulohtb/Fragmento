package com.pgalaxyp.fragmento.combat.inputModule.api;

public record InputDecision(boolean consumeVanilla) {
    private static final InputDecision PASS_THROUGH = new InputDecision(false);
    private static final InputDecision CONSUME = new InputDecision(true);
    public static InputDecision passThrough() { return PASS_THROUGH; }
    public static InputDecision consume() { return CONSUME; }
}