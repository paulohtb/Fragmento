package com.pgalaxyp.fragmento;

import com.pgalaxyp.fragmento.NEW.NewDataComponents;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import com.pgalaxyp.fragmento.registry.ItensRegistry;
import com.pgalaxyp.fragmento.setup.ClientSetup;
import com.pgalaxyp.fragmento.util.BardComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Fragmento.MODID)
public class Fragmento {

    public static final String MODID = "fragmento";

    public Fragmento(IEventBus modEventBus) {
        ItensRegistry.register(modEventBus);
        EntitiesRegistry.register(modEventBus);
        EffectsRegistry.register(modEventBus);

        NewDataComponents.COMPONENTS.register(modEventBus);
        BardComponents.COMPONENTS.register(modEventBus);

        modEventBus.addListener(ClientSetup::rendererRegister);
    }
}
