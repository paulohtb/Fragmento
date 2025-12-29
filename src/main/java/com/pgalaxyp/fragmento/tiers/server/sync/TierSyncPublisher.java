package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.tiers.common.network.TierSyncPacket;
import com.pgalaxyp.fragmento.tiers.common.service.TierSnapshot;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class TierSyncPublisher {

    public void publish(UUID playerId, TierSnapshot snapshot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(snapshot, "snapshot");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerPlayer sp = server.getPlayerList().getPlayer(playerId);
        if (sp == null) {
            return;
        }

        PacketDistributor.sendToPlayer(sp, new TierSyncPacket(snapshot.tier(), snapshot.version()));
    }
}