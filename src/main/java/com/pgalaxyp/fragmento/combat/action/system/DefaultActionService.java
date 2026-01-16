package com.pgalaxyp.fragmento.combat.action.system;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.action.runtime.*;
import java.util.*;
import java.util.function.*;

public final class DefaultActionService implements ActionService {

    private final Function<ActionId, Optional<ActionDef>> definitions;
    private final ActionStore store = new ActionStore();

    public DefaultActionService(Function<ActionId, Optional<ActionDef>> definitions) {
        this.definitions = Objects.requireNonNull(definitions);
    }

    @Override
    public ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request) {

        if (request instanceof ActionRequest.Start(var actionId)) {
            if (store.hasActive(actorId)) return ActionOutcome.reject();

            ActionDef def = definitions.apply(actionId).orElse(null);
            if (def == null) return ActionOutcome.reject();

            ActionRuntime rt = create(def.plan());
            ActionOutcome out = rt.handle(actorId, weaponId, frameId, request);
            if (out instanceof ActionOutcome.Success s && !s.finished()) store.put(actorId, rt);
            return out;
        }

        ActionRuntime rt = store.get(actorId).orElse(null);
        if (rt == null) return ActionOutcome.reject();

        ActionOutcome out = rt.handle(actorId, weaponId, frameId, request);
        if (out instanceof ActionOutcome.Success s && s.finished()) store.clear(actorId);
        return out;
    }

    private static ActionRuntime create(ActionPlan plan) {
        return switch (plan) {
            case InstantActionPlan p -> new InstantActionRuntime(p);
            case TimedSequenceActionPlan p -> new TimedSequenceActionRuntime(p);
        };
    }

    @Override public boolean hasActive(ActorId actorId) { return store.hasActive(actorId); }
    @Override public void clear(ActorId actorId) { store.clear(actorId); }
    @Override public void clearAll() { store.clearAll(); }
}