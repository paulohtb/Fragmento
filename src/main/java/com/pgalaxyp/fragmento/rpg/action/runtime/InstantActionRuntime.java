package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.emit.*;
import com.pgalaxyp.fragmento.rpg.action.result.*;
import com.pgalaxyp.fragmento.rpg.action.context.*;
import com.pgalaxyp.fragmento.rpg.action.command.*;
import com.pgalaxyp.fragmento.rpg.action.executor.*;
import java.util.*;

public final class InstantActionRuntime implements ActionRuntime {

    private final ActionRunId executionId;
    private final ActionDefinition definition;
    private boolean started;
    private boolean finished;

    public InstantActionRuntime(ActionDefinition definition) {
        this.definition = Objects.requireNonNull(definition, "definition cannot be null");
        this.executionId = ActionRunId.create();
    }

    @Override
    public ActionRunId executionId() {
        return executionId;
    }

    @Override
    public ActionDefinition definition() {
        return definition;
    }

    @Override
    public ActionResult handle(ActionContext context, ActionCommand command) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(command, "command cannot be null");

        if (finished) {
            return ActionResult.ignored();
        }

        return switch (command) {
            case ActionCancel ignored -> handleCancel();
            case ActionStart start -> handleStart(start);
            default -> ActionResult.rejected();
        };
    }

    private ActionResult handleCancel() {
        if (!started) {
            return ActionResult.rejected();
        }

        finished = true;

        return ActionResult.accepted(List.of(), true);
    }

    private ActionResult handleStart(ActionStart start) {
        ActionKey actionId = start.actionKey();
        if (started) {
            return ActionResult.rejected();
        }
        if (!definition.key().equals(actionId)) {
            return ActionResult.rejected();
        }
        if (!(definition.plan() instanceof SingleActionPlan plan)) {
            return ActionResult.rejected();
        }

        started = true;
        finished = true;

        return ActionResult.finished(List.of(EffectEmission.of(plan.effectId())));
    }
}