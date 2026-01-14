package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.result.*;
import com.pgalaxyp.fragmento.rpg.action.command.*;
import com.pgalaxyp.fragmento.rpg.action.context.*;
import com.pgalaxyp.fragmento.rpg.action.executor.*;

public sealed interface ActionRuntime permits ComboActionRuntime, InstantActionRuntime {

    ActionRunId executionId();

    ActionDefinition definition();

    ActionResult handle(ActionContext context, ActionCommand command);
}