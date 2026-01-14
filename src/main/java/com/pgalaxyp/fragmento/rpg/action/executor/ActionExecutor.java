package com.pgalaxyp.fragmento.rpg.action.executor;

import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;

public interface ActionExecutor {

    boolean supports(ActionDefinition def);

    ActionRuntime create(ActionDefinition def);
}