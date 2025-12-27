package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import java.util.Objects;
import java.util.UUID;

public final class DefaultCosmeticSyncPublisher implements CosmeticSyncPublisher {

    @Override
    public void publish(UUID playerId, CosmeticLoadoutSnapshot snapshot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(snapshot, "snapshot");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerPlayer owner = server.getPlayerList().getPlayer(playerId);
        if (owner == null) return;

        CosmeticSyncPacket pkt =
                new CosmeticSyncPacket(playerId, snapshot.loadout(), snapshot.version());

        PacketDistributor.sendToPlayer(owner, pkt);
        PacketDistributor.sendToPlayersTrackingEntity(owner, pkt);
    }
}