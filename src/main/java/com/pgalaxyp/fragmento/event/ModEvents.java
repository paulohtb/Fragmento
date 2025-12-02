package com.pgalaxyp.fragmento.event;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
import com.pgalaxyp.fragmento.NEW.EntitiesRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Fragmento.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntitiesRegistry.AEOLUS_ANGEL.get(),
                AbstractAngel.createAttributes().build());
    }
}