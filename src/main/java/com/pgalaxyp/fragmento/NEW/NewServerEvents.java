package com.pgalaxyp.fragmento.NEW;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME)
public class NewServerEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        NewAbstractWeapon.serverTickUpdateAllAbilityWeapons();
    }
}
