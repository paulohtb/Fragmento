package com.pgalaxyp.fragmento.rpg.action.type;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import java.util.*;

public record ActionDefinition(ActionKey key, ActionType type, ActionPlan plan) {

    public ActionDefinition {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(plan, "plan cannot be null");
    }
}