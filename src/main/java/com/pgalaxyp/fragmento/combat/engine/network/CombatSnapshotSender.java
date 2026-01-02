package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class CombatSnapshotSender {

    private final ServerGamePacketListenerImpl connection;

    public CombatSnapshotSender(ServerGamePacketListenerImpl connection) {
        this.connection = connection;
    }

    public void sendSnapshot(ServerPlayer player, CombatSnapshot snapshot) {
        if (player == null || snapshot == null) {
            return;
        }
        connection.send(new CombatSnapshotPayload(snapshot));
    }
}