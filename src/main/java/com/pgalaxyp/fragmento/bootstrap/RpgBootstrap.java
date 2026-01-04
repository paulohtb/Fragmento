package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.bootstrap.logging.LogChannel;
import com.pgalaxyp.fragmento.rpg.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.engine.catalyst.BardFluteEffects;
import com.pgalaxyp.fragmento.rpg.engine.catalyst.CatalystEffectRegistry;
import com.pgalaxyp.fragmento.rpg.lock.InteractionBlocker;
import com.pgalaxyp.fragmento.rpg.network.RpgPayloadHandler;
import com.pgalaxyp.fragmento.rpg.runtime.RpgEventRouter;
import com.pgalaxyp.fragmento.rpg.runtime.RpgRuntime;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public final class RpgBootstrap {

    private RpgRuntime runtime;

    public void registerContent(IEventBus modBus) {
        RpgContent.register(modBus);
    }

    public void initRuntime() {
        if (runtime != null) return;

        runtime = new RpgRuntime();
        RpgPayloadHandler.bindRuntime(runtime);

        CatalystEffectRegistry.register(
                new CatalystFamilyId("bardo"),
                new BardFluteEffects()
        );

        NeoForge.EVENT_BUS.register(new RpgEventRouter(runtime));
        NeoForge.EVENT_BUS.register(new InteractionBlocker(runtime.sessions()));

        FragmentoLog.log(LogChannel.RUNTIME, "combat runtime init ok");
    }

    public RpgRuntime runtime() {
        return runtime;
    }
}