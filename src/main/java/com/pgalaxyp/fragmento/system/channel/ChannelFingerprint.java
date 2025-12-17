package com.pgalaxyp.fragmento.system.channel;

import net.minecraft.world.item.ItemStack;

public interface ChannelFingerprint {

    boolean matches(ItemStack stack);
}
