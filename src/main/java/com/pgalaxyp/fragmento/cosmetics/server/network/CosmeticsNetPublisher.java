package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticDeltaSyncPacket;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticListSyncPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticsPublisher;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class CosmeticsNetPublisher implements CosmeticsPublisher {

    @Override
    public void sendFull(UUID ownerId, UUID recipientId, int catalogVersion, long rosterVersion, List<CosmeticEntry> entries) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || ownerId == null || recipientId == null) return;

        server.execute(() -> {
            ServerPlayer recipient = server.getPlayerList().getPlayer(recipientId);
            if (recipient == null) return;
            PacketDistributor.sendToPlayer(recipient, new CosmeticListSyncPacket(ownerId, catalogVersion, rosterVersion, entries));
        });
    }

    @Override
    public void sendDelta(UUID ownerId, long rosterVersion, CosmeticEntry entry, boolean alsoTrackers) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || ownerId == null || entry == null) return;

        server.execute(() -> {
            ServerPlayer owner = server.getPlayerList().getPlayer(ownerId);
            if (owner == null) return;

            CosmeticDeltaSyncPacket pkt = new CosmeticDeltaSyncPacket(ownerId, rosterVersion, entry);

            PacketDistributor.sendToPlayer(owner, pkt);
            if (alsoTrackers) {
                PacketDistributor.sendToPlayersTrackingEntity(owner, pkt);
            }
        });
    }
}