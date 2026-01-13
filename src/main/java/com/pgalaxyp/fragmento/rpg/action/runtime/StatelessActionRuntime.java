package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.model.*;
import java.util.*;

public final class StatelessActionRuntime implements ActionRuntime {

    private final ActionExecutionId executionId;
    private final ActionDef definition;
    private boolean consumed;

    public StatelessActionRuntime(ActionDef definition) {
        this.executionId = ActionExecutionId.create();
        this.definition = Objects.requireNonNull(definition);
    }

    @Override
    public ActionExecutionId executionId() {
        return executionId;
    }

    @Override
    public ActionDef definition() {
        return definition;
    }

    @Override
    public ActionResult handle(ActionContext context, ActionCommand command) {
        if (consumed) {
            return ActionResult.none();
        }

        if (command instanceof StartAction && definition.spec() instanceof SingleActionSpec(ActionEffectRef effect)) {
            consumed = true;
            return ActionResult.finished(List.of(effect));
        }

        return ActionResult.none();
    }
}