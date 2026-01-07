package com.pgalaxyp.fragmento.rpg.host.minecraft.events;

import com.pgalaxyp.fragmento.rpg.host.minecraft.items.RpgItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class VanillaEventBlocker {

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (isRpgWeapon(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (isRpgWeapon(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private boolean isRpgWeapon(Player player) {
        if (player == null) return false;
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return false;
        return stack.is(RpgItemTags.RPG_WEAPON);
    }
}