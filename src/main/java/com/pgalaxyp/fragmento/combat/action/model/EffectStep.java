package com.pgalaxyp.fragmento.combat.action.model;

import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public record EffectStep(int index, EffectIntent intent) {

    public EffectStep {
        if (index < 0) throw new IllegalArgumentException();
        Objects.requireNonNull(intent);
    }
}