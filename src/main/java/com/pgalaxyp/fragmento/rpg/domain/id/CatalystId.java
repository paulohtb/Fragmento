package com.pgalaxyp.fragmento.rpg.domain.id;

public record CatalystId(int value) {
    public CatalystId {
        if (value < 0) {
            throw new IllegalArgumentException("CatalystId negativo");
        }
    }
}