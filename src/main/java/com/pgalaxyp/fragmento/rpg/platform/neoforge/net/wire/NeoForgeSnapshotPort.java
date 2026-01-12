package com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.rpg.ports.SnapshotPort;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;

public final class NeoForgeSnapshotPort implements SnapshotPort {

    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = NeoForgeNetWire.encodeSnapshot(snapshot);
        NeoForgeNetWire.sendSnapshotToAll(encoded);
    }
}