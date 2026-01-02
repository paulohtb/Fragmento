package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CombatSnapshotSender {

    public void send(ServerPlayer player, CombatSnapshot snapshot) {
        if (player == null || snapshot == null) {
            return;
        }
        PacketDistributor.sendToPlayer(player, new CombatSnapshotPayload(snapshot));
    }
}