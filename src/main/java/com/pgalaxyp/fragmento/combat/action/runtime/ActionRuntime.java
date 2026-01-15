package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.model.*;

public sealed interface ActionRuntime permits InstantActionRuntime, TimedSequenceActionRuntime {
    ActionRunId runId();
    ActionDef definition();
    ActionOutcome handle(ActionContext context, ActionRequest request);
}