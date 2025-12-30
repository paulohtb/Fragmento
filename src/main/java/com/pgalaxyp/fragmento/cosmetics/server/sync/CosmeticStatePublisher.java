package com.pgalaxyp.fragmento.cosmetics.server.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedStatePublisher;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncPacket;
import com.pgalaxyp.fragmento.cosmetics.common.sync.CosmeticStateView;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import java.util.UUID;

public final class CosmeticStatePublisher
        implements VersionedStatePublisher<CosmeticStateView> {

    @Override
    public void publish(UUID playerId, CosmeticStateView state) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerPlayer owner = server.getPlayerList().getPlayer(playerId);
        if (owner == null) {
            return;
        }

        CosmeticSyncPacket pkt =
                new CosmeticSyncPacket(playerId, state.loadout(), state.version());

        PacketDistributor.sendToPlayer(owner, pkt);
        PacketDistributor.sendToPlayersTrackingEntity(owner, pkt);
    }
}