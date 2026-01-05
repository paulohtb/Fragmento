package com.pgalaxyp.fragmento.rpg.domain.execution;

public record ExecutionFacts(
        boolean hasActiveCombatEntity
) {

    public static ExecutionFacts empty() {
        return new ExecutionFacts(false);
    }
}