package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.emit.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import java.util.*;

public final class TimedSequenceActionRuntime implements ActionRuntime {

    private final ActionRunId id = ActionRunId.create();
    private final ActionDef definition;
    private int index = -1;
    private long lastFrame = -1;
    private boolean finished;

    public TimedSequenceActionRuntime(ActionDef definition) { this.definition = Objects.requireNonNull(definition); }

    @Override
    public ActionRunId runId() { return id; }

    @Override
    public ActionDef definition() { return definition; }

    @Override
    public ActionOutcome handle(ActionContext context, ActionRequest request) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(request);

        if (finished) return ActionOutcome.ignored();

        if (request instanceof ActionRequest.Cancel) {
            finished = true;
            return ActionOutcome.accepted(List.of(), true);
        }

        if (!(definition.plan() instanceof TimedSequenceActionPlan plan)) return ActionOutcome.rejected();

        if (request instanceof ActionRequest.Start || request instanceof ActionRequest.Tick) {
            if (index == -1) index = 0;
            else {
                long elapsed = context.frameId() - lastFrame;
                if (elapsed > plan.windowFrames()) {
                    finished = true;
                    return ActionOutcome.accepted(List.of(), true);
                }
                index++;
            }

            if (index >= plan.steps().size()) {
                finished = true;
                return ActionOutcome.accepted(List.of(), true);
            }

            lastFrame = context.frameId();
            EffectStep step = plan.steps().get(index);
            boolean isLast = index == plan.steps().size() - 1;
            if (isLast) finished = true;

            return ActionOutcome.accepted(List.of(EffectIntentEmission.of(step.intent())), isLast);
        }

        return ActionOutcome.rejected();
    }
}