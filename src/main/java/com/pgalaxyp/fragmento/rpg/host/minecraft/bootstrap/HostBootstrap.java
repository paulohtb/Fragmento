package com.pgalaxyp.fragmento.rpg.host.minecraft.bootstrap;

import com.pgalaxyp.fragmento.rpg.core.rule.BasicRuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionOrchestrator;
import com.pgalaxyp.fragmento.rpg.core.rule.interrupt.BasicInterruptRule;
import com.pgalaxyp.fragmento.rpg.engine.input.IntentQueue;
import com.pgalaxyp.fragmento.rpg.engine.lifecycle.EngineLoop;
import com.pgalaxyp.fragmento.rpg.engine.store.CombatStateStore;
import com.pgalaxyp.fragmento.rpg.engine.store.SnapshotStore;
import com.pgalaxyp.fragmento.rpg.engine.time.TimeSource;
import com.pgalaxyp.fragmento.rpg.platform.api.content.ActionResolver;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import net.neoforged.bus.api.IEventBus;

public final class HostBootstrap {

    public static EngineLoop init(
            IEventBus bus,
            TimeSource time,
            ActionResolver actions,
            WorldQueryGateway world
    ) {
        var actionRules = new ActionOrchestrator();

        var dispatcher = new BasicRuleDispatcher(
                actionRules,
                new BasicInterruptRule()
        );

        return new EngineLoop(
                time,
                actions,
                dispatcher,
                world,
                new CombatStateStore(),
                new SnapshotStore(),
                new IntentQueue()
        );
    }
}