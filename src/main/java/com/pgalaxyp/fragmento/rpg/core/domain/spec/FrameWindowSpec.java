package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record FrameWindowSpec(
        int framesPerStep
) {
    public FrameWindowSpec {
        if (framesPerStep <= 0) {
            throw new IllegalArgumentException();
        }
    }
}