package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.action.emit.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.result.*;
import com.pgalaxyp.fragmento.rpg.action.context.*;
import com.pgalaxyp.fragmento.rpg.action.command.*;
import com.pgalaxyp.fragmento.rpg.action.executor.*;
import java.util.*;

public final class ComboActionRuntime implements ActionRuntime {

    private final ActionRunId executionId;
    private final ActionDefinition definition;
    private boolean started;
    private boolean finished;
    private int stepIndex = -1;
    private long lastStepFrameId = -1L;

    public ComboActionRuntime(ActionDefinition definition) {
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
            case ActionStart start -> handleStart(context, start);
            case ActionAdvance adv -> handleAdvance(context, adv);
        };
    }

    private ActionResult handleCancel() {
        if (!started) {
            return ActionResult.rejected();
        }

        finished = true;

        return ActionResult.accepted(List.of(), true);
    }

    private ActionResult handleStart(ActionContext context, ActionStart start) {
        ActionKey actionKey = start.actionKey();

        if (started) {
            return ActionResult.rejected();
        }
        if (!definition.key().equals(actionKey)) {
            return ActionResult.rejected();
        }
        if (!(definition.plan() instanceof ComboActionPlan plan)) {
            return ActionResult.rejected();
        }

        started = true;
        stepIndex = 0;
        lastStepFrameId = context.frameId();

        ComboActionStep step = plan.step(0);
        boolean isLast = plan.stepsTotal() == 1;
        if (isLast) {
            finished = true;
            return ActionResult.finished(List.of(EffectEmission.of(step.effectId())));
        }

        return ActionResult.accepted(List.of(EffectEmission.of(step.effectId())), false);
    }

    private ActionResult handleAdvance(ActionContext context, ActionAdvance adv) {
        if (!started) {
            return ActionResult.rejected();
        }
        if (!(definition.plan() instanceof ComboActionPlan plan)) {
            return ActionResult.rejected();
        }

        long frameId = context.frameId();
        long elapsed = Math.subtractExact(frameId, lastStepFrameId);
        if (elapsed > plan.stepWindowFrames()) {
            finished = true;
            return ActionResult.accepted(List.of(), true);
        }

        int nextIndex = stepIndex + 1;
        if (nextIndex >= plan.stepsTotal()) {
            return ActionResult.rejected();
        }

        ComboActionStep step = plan.step(nextIndex);
        if (step.input() != adv.input()) {
            return ActionResult.rejected();
        }

        stepIndex = nextIndex;
        lastStepFrameId = frameId;

        boolean isLast = nextIndex == plan.stepsTotal() - 1;
        if (isLast) {
            finished = true;
            return ActionResult.finished(List.of(EffectEmission.of(step.effectId())));
        }

        return ActionResult.accepted(List.of(EffectEmission.of(step.effectId())), false);
    }
}