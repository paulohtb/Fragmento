package com.pgalaxyp.fragmento.rpg.platform.minecraft.events;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.ids.MinecraftActorIds;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime.FragmentoServerRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class FragmentoNeoForgeEvents {

    public static void onServerTick(ServerTickEvent.Post event) {
        FragmentoServerRuntime rt = FragmentoServerRuntime.getActive();
        if (rt == null) {
            return;
        }
        rt.tick();
    }

    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) {
            return;
        }
        FragmentoServerRuntime rt = FragmentoServerRuntime.getActive();
        if (rt == null) {
            return;
        }
        ActorId id = MinecraftActorIds.fromUuid(p.getUUID());
        rt.onPlayerJoin(id);
    }

    public static void onExternalDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) {
            return;
        }
        FragmentoServerRuntime rt = FragmentoServerRuntime.getActive();
        if (rt == null) {
            return;
        }
        ActorId id = MinecraftActorIds.fromUuid(p.getUUID());
        event.setNewDamage(0f);
    }

    private FragmentoNeoForgeEvents() {}
}