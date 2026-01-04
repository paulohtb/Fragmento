package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.content.FragmentoContent;
import com.pgalaxyp.fragmento.combat.engine.runtime.FragmentoCombatRuntime;
import com.pgalaxyp.fragmento.combat.network.FragmentoNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {

    public static final String MODID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        FragmentoLog.runtime("bootstrap start modid={}", MODID);

        FragmentoContent.register(modBus);
        FragmentoLog.runtime("bootstrap content registered");

        FragmentoNetwork.register(modBus);
        FragmentoLog.runtime("bootstrap network registered");

        FragmentoCombatRuntime.init();
        FragmentoLog.runtime("bootstrap combat runtime ready");
    }
}