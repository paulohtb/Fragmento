package com.pgalaxyp.fragmento.rpg.gameplay.time;

public record TemporalProgress(double t01) {
    public TemporalProgress {
        if (t01 < 0.0) t01 = 0.0;
        if (t01 > 1.0) t01 = 1.0;
    }
}