package com.pgalaxyp.fragmento.rpg.action.emit;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public record EffectEmission(EffectId effectId) implements ActionEmission {
    public EffectEmission {
        Objects.requireNonNull(effectId, "effect id cannot be null");
    }

    public static EffectEmission of(EffectId effectId) {
        return new EffectEmission(effectId);
    }
}