package com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.FragmentoMod;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.GAME)
public final class ServerEvents {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerRpgRuntime.activate(new ServerRpgRuntime(event.getServer()));
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ServerRpgRuntime.deactivate();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerRpgRuntime rt = ServerRpgRuntime.getActive();
        if (rt == null) {
            return;
        }
        rt.tick();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) {
            return;
        }
        ServerRpgRuntime rt = ServerRpgRuntime.getActive();
        if (rt == null) {
            return;
        }
        ActorId id = new ActorId(p.getUUID());
        rt.onPlayerJoin(id);
    }

    @SubscribeEvent
    public static void onExternalDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer)) {
            return;
        }
        if (ServerRpgRuntime.getActive() == null) {
            return;
        }
        event.setNewDamage(0f);
    }

    private ServerEvents() {}
}