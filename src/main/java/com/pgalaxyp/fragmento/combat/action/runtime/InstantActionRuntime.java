package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.emit.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import java.util.*;

public final class InstantActionRuntime implements ActionRuntime {

    private final ActionRunId id = ActionRunId.create();
    private final ActionDef definition;
    private boolean finished;

    public InstantActionRuntime(ActionDef definition) { this.definition = Objects.requireNonNull(definition); }

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

        if (request instanceof ActionRequest.Start) {
            if (!(definition.plan() instanceof InstantActionPlan plan)) return ActionOutcome.rejected();
            finished = true;
            return ActionOutcome.accepted(List.of(EffectIntentEmission.of(plan.intent())), true);
        }

        return ActionOutcome.rejected();
    }
}