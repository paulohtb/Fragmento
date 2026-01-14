package com.pgalaxyp.fragmento.rpg.action.registry;

import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import com.pgalaxyp.fragmento.rpg.action.executor.*;
import java.util.*;

public final class ActionRegistry {

    private final Map<ActionType, ActionExecutor> executors = new EnumMap<>(ActionType.class);

    public void register(ActionType type, ActionExecutor executor) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(executor, "executor cannot be null");

        if (executors.containsKey(type)) {
            throw new IllegalStateException("executor already registered for type " + type);
        }

        executors.put(type, executor);
    }

    public ActionRuntime createRuntime(ActionDefinition definition) {
        Objects.requireNonNull(definition, "definition cannot be null");

        ActionExecutor executor = executors.get(definition.type());
        if (executor == null) {
            throw new IllegalStateException("no executor for type " + definition.type());
        }

        if (!executor.supports(definition)) {
            throw new IllegalArgumentException("definition not supported for type " + definition.type());
        }

        return executor.create(definition);
    }
}