package com.pgalaxyp.fragmento.combat.domain.id;

import java.util.UUID;

public record PlayerId(UUID value) {
    public PlayerId {
        if (value == null) {
            throw new IllegalArgumentException("PlayerId vazio");
        }
    }
}