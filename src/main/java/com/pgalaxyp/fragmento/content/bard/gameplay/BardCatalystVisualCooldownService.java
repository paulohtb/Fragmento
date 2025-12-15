package com.pgalaxyp.fragmento.content.bard.gameplay;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class BardCatalystVisualCooldownService {

    private BardCatalystVisualCooldownService() {
    }

    public static void apply(ServerPlayer player, int ticks) {
        if (player == null) return;
        if (ticks <= 0) return;

        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof BardCatalystItem instMain) {
            BardCatalystCooldownService.applyCooldown(player, instMain, ticks);
        }

        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof BardCatalystItem instOff) {
            BardCatalystCooldownService.applyCooldown(player, instOff, ticks);
        }
    }
}
