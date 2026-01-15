package com.pgalaxyp.fragmento.combat.action.model;

import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public record InstantActionPlan(EffectIntent intent) implements ActionPlan {
    public InstantActionPlan { Objects.requireNonNull(intent); }
}