//package com.pgalaxyp.fragmento.util;
//
//import net.minecraft.server.level.ServerLevel;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.tick.ServerTickEvent;
//
//@EventBusSubscriber
//public class TimerTickHandler {
//
//    @SubscribeEvent
//    public static void onServerTick(ServerTickEvent.Post event) {
//        for (ServerLevel sLevel : event.getServer().getAllLevels()) {
//            TimerHandler.tickAll(sLevel);
//        }
//    }
//}