package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.content.combo.FluteComboSequence;
import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboActionRule;
import com.pgalaxyp.fragmento.rpg.engine.loop.GameLoop;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.engine.loop.UpdateScheduler;
import com.pgalaxyp.fragmento.rpg.engine.missile.MagicMissileEngine;
import com.pgalaxyp.fragmento.rpg.host.minecraft.events.VanillaEventGate;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;

public final class ModEntrypoint {

    public ModEntrypoint(IEventBus modBus) {

        var bus = new TickBus();

        var actorIds = new ActorIds(new IdGen(0L));
        ServerInputIntentHandler.init(actorIds);

        var comboRule = new ComboActionRule(FluteComboSequence.create());

        var scheduler = new UpdateScheduler()
                .register(new MagicMissileEngine(bus));

        var loop = new GameLoop(System::nanoTime, scheduler, bus);

        NeoForge.EVENT_BUS.register(new GameLoopBridge(loop));
        NeoForge.EVENT_BUS.register(new VanillaEventGate());

        new DamageBridge(actorIds, bus);
    }
}