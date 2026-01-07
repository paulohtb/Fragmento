package com.pgalaxyp.fragmento.rpg.host.minecraft.bootstrap;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.rule.BasicRuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionOrchestrator;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.BasicComboProgressionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.effect.BasicEffectRule;
import com.pgalaxyp.fragmento.rpg.core.rule.interrupt.BasicInterruptRule;
import com.pgalaxyp.fragmento.rpg.core.rule.priority.BasicPriorityRule;
import com.pgalaxyp.fragmento.rpg.engine.input.IntentQueue;
import com.pgalaxyp.fragmento.rpg.engine.lifecycle.EngineLoop;
import com.pgalaxyp.fragmento.rpg.engine.store.CombatStateStore;
import com.pgalaxyp.fragmento.rpg.engine.store.SnapshotStore;
import com.pgalaxyp.fragmento.rpg.engine.time.TimeSource;
import com.pgalaxyp.fragmento.rpg.host.minecraft.effect.EffectRegistry;
import com.pgalaxyp.fragmento.rpg.host.minecraft.effect.MinecraftEffectExecutor;
import com.pgalaxyp.fragmento.rpg.host.minecraft.effect.impl.MagicMissileEffect;
import com.pgalaxyp.fragmento.rpg.host.minecraft.input.MinecraftInputAdapter;
import com.pgalaxyp.fragmento.rpg.host.minecraft.lifecycle.ServerCombatLoop;
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
        var actionRules = new ActionOrchestrator(
                new BasicPriorityRule(),
                new BasicComboProgressionRule(),
                (timeline, startedAt) -> startedAt + Math.max(timeline.totalMillis(), 0L)
        );

        var dispatcher = new BasicRuleDispatcher(
                actionRules,
                new BasicInterruptRule(),
                new BasicEffectRule()
        );

        var engine = new EngineLoop(
                time,
                actions,
                dispatcher,
                world,
                new CombatStateStore(),
                new SnapshotStore(),
                new IntentQueue()
        );

        var effects = new EffectRegistry();
        effects.register(new EffectId("magic_missile"), new MagicMissileEffect());

        var executor = new MinecraftEffectExecutor(effects);
        var serverLoop = new ServerCombatLoop(engine, executor);

        bus.register(new MinecraftInputAdapter(serverLoop));

        return engine;
    }
}