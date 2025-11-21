package com.pgalaxyp.fragmento.event;

import com.pgalaxyp.fragmento.item.bard_weapon.AbstractBardWeapon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = "fragmento")
public class BardWeaponEvents {

    private static boolean isBardWeapon(ItemStack stack) {
        return stack.getItem() instanceof AbstractBardWeapon;
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (isBardWeapon(player.getMainHandItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (isBardWeapon(player.getMainHandItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (isBardWeapon(player.getMainHandItem())) {
            event.setCanceled(true);
        }
    }
}