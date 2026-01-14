package com.pgalaxyp.fragmento.rpg.combo.model;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboStep(int index, ComboInput input, EffectId effectId, String animationId) {

    public ComboStep {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        if (input == null || effectId == null || animationId == null) {
            throw new IllegalArgumentException();
        }
        if (animationId.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}