package com.pgalaxyp.fragmento.system.gameplay;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public final class BardCatalystCooldownService {

    private BardCatalystCooldownService() {
    }

    public static boolean isOnCooldown(ServerPlayer player, Item instrument) {
        return player.getCooldowns().isOnCooldown(instrument);
    }

    public static void applyCooldown(ServerPlayer player, Item instrument, int ticks) {
        if (ticks <= 0) return;
        player.getCooldowns().addCooldown(instrument, ticks);
    }
}
