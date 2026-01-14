package com.pgalaxyp.fragmento.rpg.action.executor;

import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import java.util.*;

public final class ComboActionExecutor implements ActionExecutor {

    @Override
    public boolean supports(ActionDefinition definition) {
        Objects.requireNonNull(definition, "definition cannot be null");
        return definition.type() == ActionType.COMBO && definition.plan() instanceof ComboActionPlan;
    }

    @Override
    public ActionRuntime create(ActionDefinition definition) {
        Objects.requireNonNull(definition, "definition cannot be null");
        if (!supports(definition)) {
            throw new IllegalArgumentException("definition not supported by this executor");
        }

        return new ComboActionRuntime(definition);
    }
}