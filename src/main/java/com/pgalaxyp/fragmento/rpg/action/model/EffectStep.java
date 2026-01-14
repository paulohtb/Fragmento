package com.pgalaxyp.fragmento.rpg.action.model;

import com.pgalaxyp.fragmento.rpg.effect.model.*;
import java.util.*;

public record EffectStep(int index, EffectIntent intent) {

    public EffectStep {
        if (index < 0) throw new IllegalArgumentException();
        Objects.requireNonNull(intent);
    }
}