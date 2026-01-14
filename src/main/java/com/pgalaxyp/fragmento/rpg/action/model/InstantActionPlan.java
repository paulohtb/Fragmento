package com.pgalaxyp.fragmento.rpg.action.model;

import com.pgalaxyp.fragmento.rpg.effect.model.*;
import java.util.*;

public record InstantActionPlan(EffectIntent intent) implements ActionPlan {
    public InstantActionPlan { Objects.requireNonNull(intent); }
}