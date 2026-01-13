package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.model.*;

public final class DefaultActionExecutor implements ActionExecutor {

    @Override
    public boolean supports(ActionDef def) {
        return def.kind() == ActionKind.INSTANT;
    }

    @Override
    public ActionRuntime create(ActionDef def) {
        return new StatelessActionRuntime(def);
    }
}