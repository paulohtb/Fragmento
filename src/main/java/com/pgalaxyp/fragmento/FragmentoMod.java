package com.pgalaxyp.fragmento;

import com.pgalaxyp.fragmento.feature.bard_class.BardModule;
import com.pgalaxyp.fragmento.feature.bard_class.client.BardClientModule;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod("fragmento")
public final class FragmentoMod {

    public FragmentoMod(IEventBus modBus) {
        BardModule.init(modBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BardClientModule.init(modBus);
        }
    }
}
