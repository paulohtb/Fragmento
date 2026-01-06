package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.engine.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import java.util.Objects;

public final class ActionIntentDispatcher {

    public ActionIntentDispatcher(ActionRuleDispatcher resolver, TickBus bus) {
        Objects.requireNonNull(resolver);
        Objects.requireNonNull(bus);
        bus.subscribe(ActionIntent.class, resolver::onAttackIntent);
    }
}