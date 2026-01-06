package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.engine.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboActionRule;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActorActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboProgressState;
import com.pgalaxyp.fragmento.rpg.engine.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import java.util.Objects;

public final class ActionRuleDispatcher {

    private final ActorActionState actions;
    private final ComboProgressState combos;
    private final ComboActionRule comboRule;
    private final TickBus bus;

    private GameTick currentTick;

    public ActionRuleDispatcher(
            ActorActionState actions,
            ComboProgressState combos,
            ComboActionRule comboRule,
            TickBus bus
    ) {
        this.actions = Objects.requireNonNull(actions);
        this.combos = Objects.requireNonNull(combos);
        this.comboRule = Objects.requireNonNull(comboRule);
        this.bus = Objects.requireNonNull(bus);

        bus.subscribe(GameTick.class, t -> currentTick = t);
    }

    public void onAttackIntent(ActionIntent intent) {
        if (currentTick == null) return;

        comboRule
                .apply(intent, actions, combos, currentTick.nowNanos())
                .ifPresent(bus::publish);
    }
}