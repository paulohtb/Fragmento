package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CombatSnapshotSender {

    public void send(ServerPlayer player, CombatSnapshot snapshot) {
        if (player == null || snapshot == null || snapshot.version() == null) {
            FragmentoLog.snapshot(
                    "snapshotSender send ignore, playerNull={} snapshotNull={} versionNull={}",
                    player == null,
                    snapshot == null,
                    snapshot != null && snapshot.version() == null
            );
            return;
        }

        FragmentoLog.snapshot(
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
            FragmentoLog.snapshot(
                    "snapshotSender send ok, player.uuid={} version={}",
                    player.getUUID(),
                    snapshot.version().value()
            );
        } catch (Throwable t) {
            FragmentoLog.snapshotEx(
                    t,
                    "snapshotSender send failed, player.uuid={} version={}",
                    player.getUUID(),
                    snapshot.version().value()
            );
        }
    }
}