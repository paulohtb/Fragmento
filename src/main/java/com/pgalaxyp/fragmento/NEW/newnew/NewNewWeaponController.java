package com.pgalaxyp.fragmento.NEW.newnew;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class NewNewWeaponController {

    private NewNewWeaponController() {}

    public static void handleLeftClick(ServerPlayer player) {

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        if (stack.getItem() instanceof NewNewAbstractWeapon weapon) {
            weapon.performTargetedAttack(player);
        }
    }
}