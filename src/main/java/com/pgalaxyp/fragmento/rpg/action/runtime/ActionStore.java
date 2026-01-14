package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.api.*;
import com.pgalaxyp.fragmento.rpg.action.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;
import java.util.function.*;

public final class ActionStore implements ActionService {

    private final Function<ActionId, Optional<ActionDef>> definition;
    private final Map<ActorId, ActionRuntime> activeByActor = new HashMap<>();

    public ActionStore(Function<ActionId, Optional<ActionDef>> definition) {
        this.definition = Objects.requireNonNull(definition, "definition cannot be null");
    }

    @Override
    public ActionOutcome handle(ActionContext context, ActionRequest request) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(request, "request cannot be null");

        ActorId actorId = context.actorId();

        if (request instanceof ActionRequest.Start(var actionId)) {
            if (activeByActor.containsKey(actorId)) return ActionOutcome.rejected();

            ActionDef definition = this.definition.apply(actionId).orElse(null);
            if (definition == null) return ActionOutcome.rejected();

            ActionRuntime runtime = createRuntime(definition);
            ActionOutcome outcome = runtime.handle(context, request);
            if (outcome instanceof ActionOutcome.Accepted accepted && !accepted.finished()) activeByActor.put(actorId, runtime);
            return outcome;
        }

        ActionRuntime runtime = activeByActor.get(actorId);
        if (runtime == null) return request instanceof ActionRequest.Cancel ? ActionOutcome.ignored() : ActionOutcome.rejected();

        ActionOutcome outcome = runtime.handle(context, request);
        if (outcome instanceof ActionOutcome.Accepted accepted && accepted.finished()) activeByActor.remove(actorId);
        return outcome;
    }

    private static ActionRuntime createRuntime(ActionDef def) {
        return switch (def.plan()) {
            case InstantActionPlan plan -> new InstantActionRuntime(def);
            case TimedSequenceActionPlan plan -> new TimedSequenceActionRuntime(def);
        };
    }

    @Override
    public boolean hasActive(ActorId actorId) {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        return activeByActor.containsKey(actorId);
    }

    @Override
    public void clear(ActorId actorId) {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        activeByActor.remove(actorId);
    }

    @Override
    public void clearAll() { activeByActor.clear(); }
}