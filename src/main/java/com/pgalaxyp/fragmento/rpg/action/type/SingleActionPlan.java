package com.pgalaxyp.fragmento.rpg.action.type;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public record SingleActionPlan(EffectId effectId) implements ActionPlan {
    public SingleActionPlan {
        Objects.requireNonNull(effectId, "effect id cannot be null");
    }
}