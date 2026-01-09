package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record StepWindowSpec(
        int framesPerStep
) {
    public StepWindowSpec {
        if (framesPerStep <= 0) {
            throw new IllegalArgumentException();
        }
    }
}