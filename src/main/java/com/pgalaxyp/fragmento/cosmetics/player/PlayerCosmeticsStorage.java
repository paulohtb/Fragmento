package com.pgalaxyp.fragmento.cosmetics.player;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticsRegistries;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class PlayerCosmeticsStorage {

    private static final Logger LOGGER = LogUtils.getLogger();

    private PlayerCosmeticsStorage() {
    }

    public static PlayerCosmeticsAttachment get(ServerPlayer player) {
        PlayerCosmeticsAttachment att = player.getData(CosmeticsRegistries.PLAYER_COSMETICS.get());
        LOGGER.debug("PlayerCosmeticsStorage get player {}", player.getUUID());
        return att;
    }
}