package com.pgalaxyp.fragmento.tiers.server.sync;

import java.util.Objects;
import java.util.UUID;
import com.pgalaxyp.fragmento.tiers.common.network.TierLevelSyncPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import com.pgalaxyp.fragmento.tiers.common.model.Tier;

public final class TierSyncPublisher {

    public void publish(UUID playerId, Tier tier, long version) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(tier, "tier");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerPlayer sp = server.getPlayerList().getPlayer(playerId);
        if (sp == null) {
            return;
        }

        int level = tier.level().value();

        PacketDistributor.sendToPlayer(
                sp,
                new TierLevelSyncPacket(level, version)
        );
    }
}