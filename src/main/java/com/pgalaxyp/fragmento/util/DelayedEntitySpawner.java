//package com.pgalaxyp.fragmento.util;
//
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.tick.ServerTickEvent;
//import net.minecraft.world.entity.projectile.Projectile;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Supplier;
//
//@EventBusSubscriber
//public class DelayedEntitySpawner {
//
//    private static final List<SpawnTask> SPAWN_TASKS = new ArrayList<>();
//
//    private record SpawnTask(int delay, Supplier<Projectile> factory) {
//        SpawnTask tick() { return new SpawnTask(delay - 1, factory); }
//    }
//
//    public static void schedule(Supplier<Projectile> projectileFactory, int delay) {
//        SPAWN_TASKS.add(new SpawnTask(delay, projectileFactory));
//    }
//
//    @SubscribeEvent
//    public static void onServerTick(ServerTickEvent.Post event) {
//        List<SpawnTask> newTasks = new ArrayList<>();
//        for (SpawnTask task : SPAWN_TASKS) {
//            if (task.delay() <= 0) {
//                Projectile p = task.factory().get();
//                if (p != null && p.level() != null) {
//                    p.level().addFreshEntity(p);
//                }
//            } else {
//                newTasks.add(task.tick());
//            }
//        }
//        SPAWN_TASKS.clear();
//        SPAWN_TASKS.addAll(newTasks);
//    }
//}