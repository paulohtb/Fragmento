package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import java.util.Objects;
import java.util.UUID;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class TrackingCosmeticSyncPublisher implements CosmeticSyncPublisher {

    @Override
    public void publish(UUID playerId, CosmeticLoadoutSnapshot snapshot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(snapshot, "snapshot");

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerPlayer owner = server.getPlayerList().getPlayer(playerId);
        if (owner == null) {
            return;
        }

        CosmeticSyncPacket pkt = new CosmeticSyncPacket(playerId, snapshot.loadout(), snapshot.version());
        PacketDistributor.sendToPlayer(owner, pkt);
        PacketDistributor.sendToPlayersTrackingEntity(owner, pkt);
    }
}