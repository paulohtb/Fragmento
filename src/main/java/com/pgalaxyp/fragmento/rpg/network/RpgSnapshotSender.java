package com.pgalaxyp.fragmento.rpg.network;

import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class RpgSnapshotSender {

    public void send(ServerPlayer player, CombatSnapshot snapshot) {
        if (player == null || snapshot == null || snapshot.version() == null) {
            return;
        }

        PacketDistributor.sendToPlayer(player, new CombatSnapshotPayload(snapshot));
    }
}