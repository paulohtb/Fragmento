package com.pgalaxyp.fragmento.rpg.platform.minecraft.port;

import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNet;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNetBytes;
import com.pgalaxyp.fragmento.rpg.port.SnapshotPort;

public final class MinecraftSnapshotPortNeoForge implements SnapshotPort {

    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = FragmentoNetBytes.encodeSnapshot(snapshot);
        FragmentoNet.sendSnapshotToAll(encoded);
    }
}