package com.pgalaxyp.fragmento.rpg.network;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.bootstrap.logging.LogChannel;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class RpgSnapshotSender {

    public void send(ServerPlayer player, CombatSnapshot snapshot) {
        if (player == null || snapshot == null || snapshot.version() == null) {
            FragmentoLog.log(
                    LogChannel.SNAPSHOT,
                    "snapshotSender send ignore, playerNull={} snapshotNull={} versionNull={}",
                    player == null,
                    snapshot == null,
                    snapshot != null && snapshot.version() == null
            );
            return;
        }

        FragmentoLog.log(
                LogChannel.SNAPSHOT,
                "snapshotSender send start, player.name={} player.uuid={} player.eid={} version={} step={} lockKind={}",
                player.getGameProfile().getName(),
                player.getUUID(),
                player.getId(),
                snapshot.version().value(),
                snapshot.combo() != null ? snapshot.combo().stepIndex() : -1,
                snapshot.lock() != null ? snapshot.lock().actionKind() : null
        );

        try {
            PacketDistributor.sendToPlayer(player, new CombatSnapshotPayload(snapshot));
            FragmentoLog.log(
                    LogChannel.SNAPSHOT,
                    "snapshotSender send ok, player.uuid={} version={}",
                    player.getUUID(),
                    snapshot.version().value()
            );
        } catch (Throwable t) {
            FragmentoLog.logEx(
                    LogChannel.SNAPSHOT,
                    t,
                    "snapshotSender send failed, player.uuid={} version={}",
                    player.getUUID(),
                    snapshot.version().value()
            );
        }
    }
}