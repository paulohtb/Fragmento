package com.pgalaxyp.fragmento.rpg.action.emit;

import com.pgalaxyp.fragmento.rpg.effect.model.*;
import java.util.*;

public record EffectIntentEmission(EffectIntent intent) implements ActionEmission {
    public EffectIntentEmission { Objects.requireNonNull(intent, "intent cannot be null"); }
    public static EffectIntentEmission of(EffectIntent intent) { return new EffectIntentEmission(intent); }
}