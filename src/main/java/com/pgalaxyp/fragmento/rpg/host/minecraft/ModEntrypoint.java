package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.content.combo.FluteComboSequence;
import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionIntentDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.action.ActionRuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboActionRule;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActorActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboProgressState;
import com.pgalaxyp.fragmento.rpg.engine.loop.GameLoop;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.engine.loop.UpdateScheduler;
import com.pgalaxyp.fragmento.rpg.engine.missile.MagicMissileEngineSystem;
import com.pgalaxyp.fragmento.rpg.host.minecraft.events.VanillaEventGate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import java.util.Random;

public final class ModEntrypoint {

    public ModEntrypoint(IEventBus modBus) {

        var bus = new TickBus();

        var actorIds = new ActorIds(new IdGen(0L));
        ServerInputIntentHandler.init(actorIds);

        var comboSequence = FluteComboSequence.create();
        var actionState = new ActorActionState();
        var comboState = new ComboProgressState();

        var comboRule = new ComboActionRule(comboSequence);
        var resolver = new ActionRuleDispatcher(
                actionState,
                comboState,
                comboRule,
                bus
        );
        new ActionIntentDispatcher(resolver, bus);

        var missileSystem = new MagicMissileEngineSystem(
                new MinecraftTargetingAdapter(actorIds),
                new Random(),
                bus
        );

        var scheduler = new UpdateScheduler()
                .register(missileSystem);

        var loop = new GameLoop(System::nanoTime, scheduler, bus);

        NeoForge.EVENT_BUS.register(new GameLoopBridge(loop));
        NeoForge.EVENT_BUS.register(new VanillaEventGate());
    }
}