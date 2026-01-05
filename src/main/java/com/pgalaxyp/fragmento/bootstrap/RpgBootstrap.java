package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.rpg.network.RpgPayloadHandler;
import com.pgalaxyp.fragmento.rpg.runtime.RpgEventRouter;
import com.pgalaxyp.fragmento.rpg.runtime.RpgRuntime;
import net.neoforged.neoforge.common.NeoForge;

public final class RpgBootstrap {

    public static void init() {
        RpgRuntime runtime = new RpgRuntime();
        RpgServices.bind(runtime);

        RpgPayloadHandler.bindRuntime(runtime);
        NeoForge.EVENT_BUS.register(new RpgEventRouter(runtime));
    }

    private RpgBootstrap() {}
}