package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record CycleSpec(
        ComboSpec combo,
        TargetingSpec targeting,
        FrameWindowSpec frameWindow
) {
    public CycleSpec {
        if (combo == null || targeting == null || frameWindow == null) {
            throw new IllegalArgumentException();
        }
    }
}