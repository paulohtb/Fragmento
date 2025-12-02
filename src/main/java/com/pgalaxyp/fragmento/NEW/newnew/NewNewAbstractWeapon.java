package com.pgalaxyp.fragmento.NEW.newnew;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public abstract class NewNewAbstractWeapon extends Item {

    public NewNewAbstractWeapon(Properties props) {
        super(props.stacksTo(1));
    }

    public abstract void performTargetedAttack(ServerPlayer player);
}