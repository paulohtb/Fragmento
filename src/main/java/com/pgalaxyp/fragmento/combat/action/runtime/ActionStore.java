package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;
import java.util.function.*;

public final class ActionStore implements ActionService {

    private final Function<ActionId, Optional<ActionDef>> definitions;
    private final Map<ActorId, ActionRuntime> active = new HashMap<>();

    public ActionStore(Function<ActionId, Optional<ActionDef>> definitions) {
        this.definitions = Objects.requireNonNull(definitions);
    }

    @Override
    public ActionOutcome handle(ActionContext context, ActionRequest request) {
        ActorId actorId = context.actorId();

        if (request instanceof ActionRequest.Start(var actionId)) {
            if (active.containsKey(actorId)) { return ActionOutcome.rejected(); }

            ActionDef definition = definitions.apply(actionId).orElse(null);
            if (definition == null) { return ActionOutcome.rejected(); }

            ActionRuntime runtime = create(definition);
            ActionOutcome outcome = runtime.handle(context, request);
            if (outcome instanceof ActionOutcome.Accepted accepted && !accepted.finished()) { active.put(actorId, runtime); }

            return outcome;
        }

        ActionRuntime runtime = active.get(actorId);
        if (runtime == null) {
            return request instanceof ActionRequest.Cancel ? ActionOutcome.ignored() : ActionOutcome.rejected();
        }

        ActionOutcome outcome = runtime.handle(context, request);
        if (outcome instanceof ActionOutcome.Accepted accepted && accepted.finished()) {
            active.remove(actorId);
        }

        return outcome;
    }

    private static ActionRuntime create(ActionDef def) {
        return switch (def.plan()) {
            case InstantActionPlan plan -> new InstantActionRuntime(def);
            case TimedSequenceActionPlan plan -> new TimedSequenceActionRuntime(def);
        };
    }

    @Override
    public boolean hasActive(ActorId actorId) { return active.containsKey(actorId); }

    @Override
    public void clear(ActorId actorId) { active.remove(actorId); }

    @Override
    public void clearAll() { active.clear(); }
}