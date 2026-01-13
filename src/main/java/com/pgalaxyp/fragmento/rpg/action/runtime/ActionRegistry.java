package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.model.*;
import java.util.*;

public final class ActionRegistry {

    private final List<ActionExecutor> executors = new ArrayList<>();

    public void register(ActionExecutor executor) {
        executors.add(Objects.requireNonNull(executor));
    }

    public ActionRuntime createRuntime(ActionDef def) {
        for (var e : executors) {
            if (e.supports(def)) {
                return e.create(def);
            }
        }
        throw new IllegalStateException();
    }
}