package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.bootstrap.logging.LogChannel;
import com.pgalaxyp.fragmento.rpg.network.RpgNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {

    public static final String MODID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        FragmentoLog.initFileLogging();
        FragmentoLog.log(LogChannel.RUNTIME, "bootstrap start modid={}", MODID);

        RpgBootstrap bootstrap = new RpgBootstrap();

        bootstrap.registerContent(modBus);
        FragmentoLog.log(LogChannel.RUNTIME, "bootstrap content registered");

        RpgNetwork.register(modBus);
        FragmentoLog.log(LogChannel.RUNTIME, "bootstrap network registered");

        bootstrap.initRuntime();
        FragmentoLog.log(LogChannel.RUNTIME, "bootstrap combat runtime ready");
    }
}