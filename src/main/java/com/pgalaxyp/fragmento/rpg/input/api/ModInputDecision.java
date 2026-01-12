package com.pgalaxyp.fragmento.rpg.input.api;

public record ModInputDecision(
        boolean consumeVanilla,
        boolean emittedDomainIntent
) {
    public ModInputDecision {}

    public static ModInputDecision passThrough() {
        return new ModInputDecision(false, false);
    }

    public static ModInputDecision consumeOnly() {
        return new ModInputDecision(true, false);
    }

    public static ModInputDecision emitAndConsume() {
        return new ModInputDecision(true, true);
    }

    public static ModInputDecision emitNoConsume() {
        return new ModInputDecision(false, true);
    }
}