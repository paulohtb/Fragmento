package com.pgalaxyp.fragmento.rpg.core.domain.time;

public record FrameContext(
        long frameId,
        int tickIndex
) {
    public FrameContext {
        if (frameId < 0 || tickIndex < 0) {
            throw new IllegalArgumentException();
        }
    }
}