package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;

public record EffectOutcome(List<StateDelta> deltas) {
    public EffectOutcome { deltas = List.copyOf(Objects.requireNonNull(deltas)); }
    public static EffectOutcome empty() { return new EffectOutcome(List.of()); }
}
