package com.pgalaxyp.fragmento.rpg_old.event;

import com.pgalaxyp.fragmento.rpg_old.registry.RpgRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public final class VanillaAttackBlocker {

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer sp)) return;

        if (catalystActive(sp)) {
            e.setCanceled(true);
        }
    }

    private static boolean catalystActive(ServerPlayer p) {
        ItemStack off = p.getItemBySlot(EquipmentSlot.OFFHAND);
        if (off != null && !off.isEmpty()) return false;

        ItemStack main = p.getItemBySlot(EquipmentSlot.MAINHAND);
        if (main == null || main.isEmpty()) return false;

        return RpgRegistry.catalysts().resolve(main) != null;
    }
}