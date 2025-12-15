package com.pgalaxyp.fragmento.gameplay.channel;

import net.minecraft.world.item.ItemStack;

public interface ChannelFingerprint {

    boolean matches(ItemStack stack);
}
