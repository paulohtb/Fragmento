package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ActorId(long value) {
    public ActorId {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }
}