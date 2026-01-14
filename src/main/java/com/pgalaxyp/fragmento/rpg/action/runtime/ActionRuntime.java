package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.api.*;
import com.pgalaxyp.fragmento.rpg.action.model.*;

public sealed interface ActionRuntime permits InstantActionRuntime, TimedSequenceActionRuntime {
    ActionRunId runId();
    ActionDef definition();
    ActionOutcome handle(ActionContext context, ActionRequest request);
}