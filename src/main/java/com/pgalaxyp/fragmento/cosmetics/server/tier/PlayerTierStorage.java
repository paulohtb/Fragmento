package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticsRegistries;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class PlayerTierStorage {

    private static final Logger LOGGER = LogUtils.getLogger();

    private PlayerTierStorage() {
    }

    public static PlayerTierAttachment get(ServerPlayer player) {
        PlayerTierAttachment att = player.getData(CosmeticsRegistries.PLAYER_TIER.get());
        LOGGER.debug("PlayerTierStorage get player {}", player.getUUID());
        return att;
    }
}