package com.pgalaxyp.fragmento.rpg.combo.model;

import com.pgalaxyp.fragmento.rpg.combo.api.*;

public record ComboStep(int index, ComboInput input, String animationId) {

    public ComboStep {
        if (index < 0 || input == null || animationId == null || animationId.isBlank())
            throw new IllegalArgumentException();
    }
}