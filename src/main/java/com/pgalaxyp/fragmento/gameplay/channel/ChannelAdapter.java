package com.pgalaxyp.fragmento.gameplay.channel;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public interface ChannelAdapter {

    ChannelFingerprint createFingerprint(ItemStack stack);

    ChannelEntity resolveEntity(ServerLevel level, int entityId);

    void applyVisualCooldown(ServerPlayer player, int ticks);

    boolean isStillHolding(ServerPlayer player, InteractionHand hand, ChannelFingerprint fingerprint);
}
