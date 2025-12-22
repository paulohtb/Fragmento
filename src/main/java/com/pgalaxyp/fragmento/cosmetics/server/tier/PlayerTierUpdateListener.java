package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerTierUpdateListener {
    void onTierApplied(MinecraftServer server, ServerPlayer player, CosmeticTier tier);
}