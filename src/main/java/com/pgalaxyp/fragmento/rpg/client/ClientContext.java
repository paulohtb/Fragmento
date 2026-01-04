package com.pgalaxyp.fragmento.rpg.client;

import com.pgalaxyp.fragmento.rpg.content.catalyst.FluteItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class ClientContext {

    public static boolean inGame(Minecraft mc) {
        return mc != null && mc.player != null && mc.options != null && mc.screen == null;
    }

    public static boolean catalystActive(Minecraft mc) {
        if (mc == null || mc.player == null) return false;

        ItemStack main = mc.player.getMainHandItem();
        ItemStack off = mc.player.getOffhandItem();
        return FluteItem.isFlute(main) && off.isEmpty();
    }

    private ClientContext() {}
}