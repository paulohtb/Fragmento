package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.model.*;

public sealed interface ActionRuntime permits StatelessActionRuntime {

    ActionExecutionId executionId();
    ActionDef definition();
    ActionResult handle(ActionContext context, ActionCommand command);
}