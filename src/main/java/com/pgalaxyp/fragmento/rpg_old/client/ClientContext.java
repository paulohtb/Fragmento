package com.pgalaxyp.fragmento.rpg_old.client;

import com.pgalaxyp.fragmento.rpg_old.content.catalyst.FluteItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public final class ClientContext {

    public static boolean inGame(Minecraft mc) {
        return mc != null && mc.level != null && mc.player != null;
    }

    public static boolean catalystActive(Minecraft mc) {
        if (!inGame(mc)) return false;

        LocalPlayer p = mc.player;
        if (p == null) return false;

        ItemStack main = p.getMainHandItem();
        ItemStack off = p.getOffhandItem();

        if (off != null && !off.isEmpty()) return false;
        return FluteItem.isFlute(main);
    }

    private ClientContext() {}
}