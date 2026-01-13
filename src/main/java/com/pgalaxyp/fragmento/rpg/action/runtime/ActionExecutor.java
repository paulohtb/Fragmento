package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.model.*;

public interface ActionExecutor {

    boolean supports(ActionDef def);
    ActionRuntime create(ActionDef def);
}