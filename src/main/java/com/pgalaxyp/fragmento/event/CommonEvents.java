package com.pgalaxyp.fragmento.event;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Fragmento.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel sLevel : event.getServer().getAllLevels()) {
            TimerHandler.tickAll(sLevel);
        }
    }
}