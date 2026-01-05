package com.pgalaxyp.fragmento.rpg.client;

import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.LoadoutSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class ClientContext {

    public static boolean inGame(Minecraft mc) {
        return mc != null && mc.player != null && mc.options != null && mc.screen == null;
    }

    public static boolean catalystActive(Minecraft mc) {
        if (mc == null || mc.player == null) return false;

        CombatSnapshot snap = ClientNetworkProxy.state().current();
        if (snap != null && snap.loadout() != null) {
            LoadoutSnapshot l = snap.loadout();
            if (l.equippedCatalyst() != null && l.family() != null && l.offhandEmpty()) {
                return true;
            }
        }

        ItemStack main = mc.player.getMainHandItem();
        ItemStack off = mc.player.getOffhandItem();

        if (off == null || !off.isEmpty()) return false;
        return RpgRegistry.catalysts().resolve(main) != null;
    }

    private ClientContext() {}
}