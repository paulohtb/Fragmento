package com.pgalaxyp.fragmento;

import com.pgalaxyp.fragmento.features.bard_class.BardClassModule;
import com.pgalaxyp.fragmento.features.bard_class.client.BardClassClientModule;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod("fragmento")
public final class FragmentoMod {

    public FragmentoMod(IEventBus modBus) {
        BardClassModule.init(modBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BardClassClientModule.init(modBus);
        }
    }
}
