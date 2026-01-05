package com.pgalaxyp.fragmento.rpg_old.event;

import com.pgalaxyp.fragmento.rpg_old.runtime.RpgRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class RpgServerEvents {

    private final RpgRuntime runtime;

    public RpgServerEvents(RpgRuntime runtime) {
        this.runtime = runtime;
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) {
            runtime.onLogout(sp);
        }
    }
}