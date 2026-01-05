package com.pgalaxyp.fragmento.rpg_old.domain.id;

public record CatalystId(int value) {
    public CatalystId {
        if (value < 0) {
            throw new IllegalArgumentException("CatalystId negativo");
        }
    }
}