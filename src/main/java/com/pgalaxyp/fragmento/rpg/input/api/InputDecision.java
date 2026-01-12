package com.pgalaxyp.fragmento.rpg.input.api;

public record InputDecision(
        boolean consumeVanilla,
        boolean emittedDomainIntent
) {
    public InputDecision {}

    public static InputDecision passThrough() {
        return new InputDecision(false, false);
    }

    public static InputDecision consumeOnly() {
        return new InputDecision(true, false);
    }

    public static InputDecision emitAndConsume() {
        return new InputDecision(true, true);
    }

    public static InputDecision emitNoConsume() {
        return new InputDecision(false, true);
    }
}