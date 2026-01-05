package com.pgalaxyp.fragmento.rpg_old.domain.execution;

public record ExecutionFacts(
        boolean hasActiveCombatEntity
) {

    public static ExecutionFacts empty() {
        return new ExecutionFacts(false);
    }
}