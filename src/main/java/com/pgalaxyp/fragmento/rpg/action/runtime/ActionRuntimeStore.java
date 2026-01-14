package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.result.*;
import com.pgalaxyp.fragmento.rpg.action.context.*;
import com.pgalaxyp.fragmento.rpg.action.command.*;
import com.pgalaxyp.fragmento.rpg.action.registry.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class ActionRuntimeStore {

    private final ActionRegistry registry;
    private final Map<ActorId, ActionRuntime> activeByActor = new HashMap<>();

    public ActionRuntimeStore(ActionRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry cannot be null");
    }

    public ActionResult handle(ActionContext context, ActionDefinition definition, ActionCommand command) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(definition, "definition cannot be null");
        Objects.requireNonNull(command, "command cannot be null");

        ActorId actorId = context.actorId();
        if (command instanceof ActionStart start) {
            return handleStart(actorId, context, definition, start);
        }

        ActionRuntime runtime = activeByActor.get(actorId);
        if (runtime == null) {
            return ActionResult.rejected();
        }

        ActionResult result = runtime.handle(context, command);
        if (result.finished()) {
            activeByActor.remove(actorId);
        }

        return result;
    }

    private ActionResult handleStart(ActorId actorId, ActionContext context, ActionDefinition definition, ActionStart start) {
        ActionKey actionKey = start.actionKey();
        if (!definition.key().equals(actionKey)) {
            return ActionResult.rejected();
        }
        if (activeByActor.containsKey(actorId)) {
            return ActionResult.rejected();
        }

        ActionRuntime runtime = registry.createRuntime(definition);
        ActionResult result = runtime.handle(context, start);
        if (result.status() == ActionStatus.ACCEPTED && !result.finished()) {
            activeByActor.put(actorId, runtime);
        }

        return result;
    }

    public boolean hasActive(ActorId actorId) {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        return activeByActor.containsKey(actorId);
    }

    public void clear(ActorId actorId) {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        activeByActor.remove(actorId);
    }

    public void clearAll() {
        activeByActor.clear();
    }
}