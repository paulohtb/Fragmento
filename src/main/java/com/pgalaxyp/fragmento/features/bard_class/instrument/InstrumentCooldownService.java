package com.pgalaxyp.fragmento.features.bard_class.instrument;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public final class InstrumentCooldownService {

    private InstrumentCooldownService() {
    }

    public static boolean isOnCooldown(ServerPlayer player, Item instrument) {
        return player.getCooldowns().isOnCooldown(instrument);
    }

    public static void applyCooldown(ServerPlayer player, Item instrument, int ticks) {
        if (ticks <= 0) {
            return;
        }
        player.getCooldowns().addCooldown(instrument, ticks);
    }
}
