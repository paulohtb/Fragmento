package com.pgalaxyp.fragmento.rpg.action.type;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboActionStep(int index, ComboInput input, EffectId effectId, String animationId) {
    public ComboActionStep {
        if (index < 0) {
            throw new IllegalArgumentException("index must be zero or positive");
        }
        if (input == null || effectId == null || animationId == null) {
            throw new IllegalArgumentException();
        }
        if (animationId.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}