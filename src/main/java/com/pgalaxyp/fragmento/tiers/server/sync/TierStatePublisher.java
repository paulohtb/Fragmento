package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedStatePublisher;
import com.pgalaxyp.fragmento.tiers.client.sync.TierLevelSyncPacket;
import com.pgalaxyp.fragmento.tiers.common.sync.TierStateView;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import java.util.UUID;

public final class TierStatePublisher implements VersionedStatePublisher<TierStateView> {

    @Override
    public void publish(UUID playerId, TierStateView state) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) {
            return;
        }

        PacketDistributor.sendToPlayer(
                player,
                new TierLevelSyncPacket(
                        state.tier().level().value(),
                        state.version()
                )
        );
    }
}