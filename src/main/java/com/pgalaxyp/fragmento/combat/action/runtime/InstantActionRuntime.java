package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import java.util.*;

public final class InstantActionRuntime implements ActionRuntime {

    private final InstantActionPlan plan;
    private boolean done;

    public InstantActionRuntime(InstantActionPlan plan) {
        this.plan = Objects.requireNonNull(plan);
    }

    @Override
    public ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request) {
        if (done) return ActionOutcome.finished(List.of());

        if (request instanceof ActionRequest.Cancel) {
            done = true;
            return ActionOutcome.finished(List.of());
        }

        if (request instanceof ActionRequest.Start) {
            done = true;
            return ActionOutcome.finished(List.of(plan.intent()));
        }

        return ActionOutcome.reject();
    }
}